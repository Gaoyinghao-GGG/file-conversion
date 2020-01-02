package com.iocasckani.project.file_conversion.controller;


import com.iocasckani.project.file_conversion.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class CTDCnvFolderConversionController {
    CnvBuiuldCsv cnvBuiuldCsv = new CnvBuiuldCsv();
    ZipFiles zipFiles = new ZipFiles();
    CnvDispose cnvDispose = new CnvDispose();

    //转化cnv文件夹
    @RequestMapping("/importCTDFolderCnv")
    @ResponseBody
    public String importFolderCnv(HttpServletRequest request) throws IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                request.setCharacterEncoding("utf-8");
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multipartRequest.getFileNames();

                MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
                // 读取文件名称
                String fileName = mfile.getOriginalFilename().toString();
                String[] str = fileName.split("/");
                int length = str.length;
                String name = str[length - 1];
                String depth = str[str.length - 2].split("_")[1].replaceAll("[a-zA-Z]", "");
                List<String> DataHeader = CTDCnvDispose.readDataHeader(mfile);
               /* for (String da:DataHeader) {
                    System.out.println(da);
                }*/
                List<Map<String, Object>> mapList = CTDCnvDispose.ReadCTdDataContent(mfile, DataHeader, depth);
                /*int aa = 0;
                for (Map<String, Object> o:mapList) {
                    System.out.println(o);
                    if(aa ==11){break;}
                    aa++;
                }*/
                String filePath = "../file/static/file/";
                List<String> Introduce = CTDCnvDispose.ReadIntroduce(mfile);
                if (CTDCnvBuiuldCsv.buildCTDCsv(DataHeader, mapList, fileName, filePath, Introduce)) {
                    return "true";
                } else {
                    return fileName;
                }

            } catch (Exception ex) {
            }
        }
        return "false";
    }

}