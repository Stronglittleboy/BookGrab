package com.hlw.webmagic.begin.beginTwo.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: BookGrab
 * @description: 下载工具
 * @author: houlongwang
 * @create: 2019-05-03 16:31
 **/
public class DownUtils {
    public static void main(String[] args) {

    }


    public static boolean downloadPicture(String imgURL, String target) {
        String[] cmds = { //
                "curl", //
                "-o", //把下载的数据保存到指定的文件中,这里是target（小写o）
                target, //
                imgURL, //需要下载的URI网址
                "-s", //silent 模式，不输出任何东西（小写s）
                "-w \n%{http_code}"//控制额外输出（小写w）
        };
        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb.redirectErrorStream(true);
        Process p;
        BufferedReader br = null;
        try {
            p = pb.start();
            String line = null;
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = br.readLine()) != null) {
                if (line.trim().contains("200")) {
                    System.out.println("Download Img 200:\t" + cmds[2]);
                    return true;
                }
                if (line.trim().contains("404")) {
                    System.out.println("Download Img 404:\t" + cmds[2]);
                    return false;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}