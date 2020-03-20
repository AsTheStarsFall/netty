package com.tianhy.request;

import java.io.Serializable;

/**
 * {@link}
 *
 * @Desc: 请求体封装
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -3891726021120283635L;
    //类名
    private String className;
    //方法名
    private String methodName;
    //参数
    private Object[] parameters;
    //版本
    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
