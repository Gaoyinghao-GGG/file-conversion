package com.iocasckani.project.file_conversion.controller;


import com.iocasckani.project.file_conversion.util.*;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ADCPConversionController {

    //转化ADBC cnv文件和文件夹
    @RequestMapping("/importADCPFile")
    @ResponseBody
    public String importFileCnv(HttpServletRequest request) throws IOException {
        //查看获取是表单还是文件
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                request.setCharacterEncoding("utf-8");
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Iterator iter = multipartRequest.getFileNames();
                MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
                System.out.println();
                String fileName = mfile.getOriginalFilename();
                String filePath = "../file/static/file/";
                if(!fileName.contains(".DAT")){return "wrongfile";}
                if(ADCPBuiuldCsv.BuildADCPCsv(mfile,fileName,filePath)){
                    return "true";
                }else {return "false";}
            }catch (Exception ex) {
                System.out.println("抛出异常");
                return  "false";
            }
        }
        return "false";
    }
}