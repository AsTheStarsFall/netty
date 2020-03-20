package com.study.netty.protocol.http.fileServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * {@link}
 *
 * @Desc: 业务逻辑处理器
 * @Author: thy
 * @CreateTime: 2019/11/25 23:04
 **/
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //解码失败
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        //非GET请求
        if (request.getMethod() != HttpMethod.GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }
//        System.out.println(request.toString());
        final String uri = request.getUri();
        //对uri进行解码
        final String path = sanitizeUri(uri);
        if (path == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        //根据路径获取到文件
        File file = new File(path);
        //没有找到文件
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }
        //如果是一个目录
        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendList(ctx, file);
            } else {
                sendRedirect(ctx, uri + "/");
            }
            return;
        }

        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            //以只读的方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            sendError(ctx, NOT_FOUND);
            return;
        }


        //response

        //文件长度
        long length = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        setContentLength(response, length);
        setContentTypeHeader(response, file);
        if (isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response);
        ChannelFuture sendFileFuture;
        //通过netty的ChunkedFile对象直接将文件写入到发送缓冲区
        sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0,
                length, 8192), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total < 0) {
                    System.err.println("Transfer progress: " + progress);
                } else {
                    System.err.println("Transfer progress: " + progress + " / " + total);
                }
            }

            //发送完成
            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete.");
            }
        });

        //如果使用ChunkedFile,最后需要发送一个编码结束的空消息体，标识所有的消息体已经发送完成
        //同时调用flush方法将之前在发送缓冲区的消息刷新到SocketChannel中发送给对方
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    //异常捕捉
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    //发送错误信息
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure :" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        //响应头
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    //设置MIME类型
    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getPath()));

    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    //对uri进行解码
    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException ex) {
                throw new Error();
            }
        }
        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }
        //将硬编码的文件路径分隔符替换为本地操作系统的文件路径分隔符
        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + '.')
                || uri.contains('.' + File.separator) || uri.startsWith(".")
                || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        //对文件进行拼接，使用当前运行程序所在的工作目录+uri 构造绝对路径返回
        return System.getProperty("user.dir") + File.separator + uri;
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    //正确的发送内容
    private static void sendList(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        String dirPath = dir.getPath();
        /**
         * <!DOCTYPE html>
         * <html lang="en">
         * <head>
         *     <meta charset="UTF-8">
         *     <title>
         *         。。。
         *         目录：
         *     </title>
         * </head>
         * <body>
         * <h3>
         *     。。。
         *     目录：
         * </h3>
         * </body>
         * </html>
         */
        sb.append("<!DOCTYPE html>\r\n");
        sb.append("<html><head><title>");
        sb.append(dirPath);
        sb.append("目录：");
        sb.append("</title></head><body>\r\n");
        sb.append("<h3>");
//        sb.append(dir).append("目录：");
        sb.append("Hello Wrold:").append("目录：");

        sb.append("</h3>\r\n");
        sb.append("<ul>");
        // ..的连接
        sb.append("<li>链接：<a href=\"../\">..</a></li>\r\n");

        //根目录下所有文件以及文件夹，使用超链接来标识
        for (File file : dir.listFiles()) {
            if (file.isHidden() || !file.canRead()) {
                continue;
            }
            //文件名
            String fileName = file.getName();
            if (!ALLOWED_FILE_NAME.matcher(fileName).matches()) {
                continue;
            }
            sb.append("<li>链接：<a href=\"");
            sb.append(fileName);
            sb.append("\">");
            sb.append(fileName);
            sb.append("</a></li>\r\n");
        }
        sb.append("</ul></body></html>\r\n");
        ByteBuf byteBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    //重定向
    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
