package com.iocasckani.project.file_conversion.controller;


import com.iocasckani.project.file_conversion.util.*;
import com.iocasckani.project.file_conversion.util.merge.quanqiu;
import com.iocasckani.project.file_conversion.util.merge.quanqiuBuild;
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
public class CnvFileConversionController {
    CnvBuiuldCsv cnvBuiuldCsv = new CnvBuiuldCsv();
    ZipFiles zipFiles = new ZipFiles();
    CnvDispose cnvDispose = new CnvDispose();

    //转化cnv文件
    @RequestMapping("/importFileCnv")
    @ResponseBody
    public String importFileCnv(HttpServletRequest request) throws IOException {
        String msg = "导入成功！<br/>";
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                request.setCharacterEncoding("utf-8");
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Iterator iter = multipartRequest.getFileNames();

                MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
                // 读取文件名称：按文件名称、站号进行区分，获取对应的站位ID
                String fileName = mfile.getOriginalFilename();
                if (!fileName.contains(".cnv")) {
                    return "wrongfile";
                }
                System.out.println(fileName);
                   /* List<Map<String, Object>> mapList = quanqiu.CNVDispose(mfile);
                for (Map<String, Object> m:mapList) {
                    System.out.println(m);
                }*/
                //String filePath = "../file/static/file/";
                //quanqiuBuild.buildCsv(mapList,fileName,filePath);
                List<String> DataHeader = CnvDispose.readDataHeader(mfile);
                List<String> Introduce = cnvDispose.ReadIntroduce(mfile);
                if (DataHeader.size() == 0) {
                    return "noHeader";
                }
                System.out.println(DataHeader.size());
                List<Map<String, Object>> mapList = CnvDispose.ReadDataContent(mfile, DataHeader);
                if (mapList.size() < 1) {
                    return "false";
                }
                System.out.println(mapList.size());
                String filePath = "../file/static/file/";
                if (!cnvBuiuldCsv.buildCsv(DataHeader, mapList, fileName, filePath, Introduce)) {
                    return "false";
                }
                return "true";
            } catch (Exception ex) {
                return "false";
            }
        }
        return null;
    }

    //转化cnv文件夹
    @RequestMapping("/importFolderCnv")
    @ResponseBody
    public String importFolderCnv(HttpServletRequest request) throws IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                request.setCharacterEncoding("utf-8");
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multipartRequest.getFileNames();
                //遍历list，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件

                MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
                // 读取文件名称：按文件名称、站号进行区分，获取对应的站位ID
                String fileName = mfile.getOriginalFilename();
                //if(!fileName.contains(".cnv")){ continue;}
                List<String> DataHeader = CnvDispose.readDataHeader(mfile);
                List<Map<String, Object>> mapList = CnvDispose.ReadDataContent(mfile, DataHeader);
                String filePath = "../file/static/file/";
                List<String> Introduce = cnvDispose.ReadIntroduce(mfile);
                if (cnvBuiuldCsv.buildCsv(DataHeader, mapList, fileName, filePath, Introduce)) {
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