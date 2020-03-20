package com.study.netty;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous BioTest :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    //网易云音乐缓存格式转换
    @Test
    public void cloudMusicCache(){
        try {
            File rFile = new File("D:\\test.mp3");
            File oFile = new File("D:\\out.mp3");

            DataInputStream dis = new DataInputStream(new FileInputStream(rFile));
            DataOutputStream dos = new DataOutputStream( new FileOutputStream(oFile));
            byte[] by = new byte[1000];
            int len;
            while((len=dis.read(by))!=-1){
                for(int i=0;i<len;i++){
                    by[i]^=0xa3;
                }
                dos.write(by,0,len);
            }
            dis.close();
            dos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
