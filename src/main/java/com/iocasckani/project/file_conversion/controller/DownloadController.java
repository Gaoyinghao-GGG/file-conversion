package com.iocasckani.project.file_conversion.controller;

import com.iocasckani.project.file_conversion.util.ZipFiles;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

@Controller
public class DownloadController {
    ZipFiles zipFiles = new ZipFiles();

    //下载文件
    @RequestMapping("/filedownload")
    public void download(@ApiParam(value = "csv", required = true) HttpServletRequest request, HttpServletResponse response, String name) {
        String filename = name.split("\\.")[0] + ".csv";
        System.out.println(filename);
        String url = "../file/static/file/" + filename;
        System.out.println(url);
        java.io.File fileurl = new java.io.File(url);
        //浏览器下载后的文件名称showValue,从url中截取到源文件名称以及，以及文件类型，如board.docx;
        String showValue = filename;
        try {
            //将文件读入文件流
            InputStream inStream;
            inStream = new FileInputStream(fileurl);
            //获得浏览器代理信息
            final String userAgent = request.getHeader("USER-AGENT");
            //判断浏览器代理并分别设置响应给浏览器的编码格式
            String finalFileName = null;
            if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {//IE浏览器
                finalFileName = URLEncoder.encode(showValue, "UTF8");
                System.out.println("IE浏览器");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
                finalFileName = new String(showValue.getBytes(), "ISO8859-1");
            } else {
                finalFileName = URLEncoder.encode(showValue, "UTF8");//其他浏览器
            }
            //设置HTTP响应头
            response.reset();//重置 响应头
            response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
            response.addHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");//下载文件的名称

            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
            response.getOutputStream().close();
            zipFiles.delFile("../file/static/file/", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("/folderdownload")
    public String downloadFolder(HttpServletRequest request, HttpServletResponse response, String foldername) {
        zipFiles.compress("../file/static/file/" + foldername, "../file/static/zipfile/" + foldername + ".zip");
        zipFiles.deleteDir("../file/static/file");
        String url = "../file/static/zipfile/" + foldername + ".zip";
        java.io.File fileurl = new java.io.File(url);
        //浏览器下载后的文件名称showValue,从url中截取到源文件名称以及，以及文件类型，如board.docx;
        String showValue = foldername + ".zip";
        try {
            //将文件读入文件流
            InputStream inStream;
            inStream = new FileInputStream(fileurl);
            //获得浏览器代理信息
            final String userAgent = request.getHeader("USER-AGENT");
            //判断浏览器代理并分别设置响应给浏览器的编码格式
            String finalFileName = foldername + ".zip";
            if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {//IE浏览器
                finalFileName = URLEncoder.encode(showValue, "UTF8");
                System.out.println("IE浏览器");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
                finalFileName = new String(showValue.getBytes(), "ISO8859-1");
            } else {
                finalFileName = URLEncoder.encode(showValue, "UTF8");//其他浏览器
            }
            //设置HTTP响应头
            response.reset();//重置 响应头
            response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
            response.addHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");//下载文件的名称

            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
            response.getOutputStream().close();
            zipFiles.delFile("../file/static/zipfile/", foldername + ".zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "true";
    }
}
