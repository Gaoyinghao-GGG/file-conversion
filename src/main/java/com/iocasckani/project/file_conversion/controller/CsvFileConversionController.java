package com.iocasckani.project.file_conversion.controller;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.iocasckani.project.file_conversion.util.CsvBuiuldCsv;
import com.iocasckani.project.file_conversion.util.CsvDispose;
import com.iocasckani.project.file_conversion.util.LocalCsv;
import com.iocasckani.project.file_conversion.util.ZipFiles;
import com.iocasckani.project.file_conversion.pojo.File;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Controller
public class CsvFileConversionController {

    CsvBuiuldCsv buiuldCsv = new CsvBuiuldCsv();
    CsvDispose fileDispose = new CsvDispose();
    ZipFiles zipFiles = new ZipFiles();
    @RequestMapping("upData")
    public String toCreateFile(){
        return "/file_conversion";
    }

    @RequestMapping("/folderConversion")
    @ResponseBody
    public String ImportGroupFile(@ApiParam(value = "csv", required = true) MultipartFile file, HttpServletRequest httpServletRequest) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;
        Iterator iter = multipartRequest.getFileNames();
        String fileHeadPath = null;
        String path = null;

            MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
            String fileName = mfile.getOriginalFilename();
            System.out.println(fileName);

            InputStream fileStream = mfile.getInputStream();

            CsvReader csvReader = new CsvReader(fileStream, ',', Charset.forName("UTF-8"));
            csvReader.readHeaders();
            List<Map<String, Object>> FileData = new ArrayList<Map<String, Object>>();
            while (csvReader.readRecord()) {
                FileData.add(fileDispose.fileDispose(csvReader));
            }
            LocalCsv.WriteCsv(FileData);
            List<File> fileList = new ArrayList<>();
            for (Map<String, Object> map : FileData) {
                File files = JSON.toJavaObject((JSON) JSON.toJSON(map), File.class);
                fileList.add(files);
            }

            fileHeadPath = fileName.substring(0,fileName.indexOf("/"));
            System.out.println(fileHeadPath);
            String filePath = fileName.substring(0,fileName.lastIndexOf("/")+1);
            fileName = fileName.split("/")[fileName.split("/").length-1];
            System.out.println(fileName);
            buiuldCsv.buildFolderCsv(fileList, fileName, filePath);

        return "true";
    }
    @RequestMapping("/conversion")
    @ResponseBody
    public String ImportFile(@ApiParam(value = "csv", required = true) MultipartFile file, HttpServletRequest httpServletRequest) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;
        Iterator iter = multipartRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
            String fileName = mfile.getOriginalFilename();
            InputStream fileStream = mfile.getInputStream();
            if (!fileName.contains("csv")) {
                return "wrongfile";
            }
            // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
            CsvReader csvReader = new CsvReader(fileStream, ',', Charset.forName("UTF-8"));
            // 这行不要是为了从表头的下一行读，也就是过滤表头
            csvReader.readHeaders();
            //根据需要导入的表来定义具体的字段
            List<Map<String, Object>> FileData = new ArrayList<Map<String, Object>>();
            while (csvReader.readRecord()) {
                FileData.add(fileDispose.fileDispose(csvReader));
            }
            System.out.println(FileData.size());
            List<File> fileList = new ArrayList<>();
            for (Map<String, Object> map : FileData) {
                File files = JSON.toJavaObject((JSON) JSON.toJSON(map), File.class);
                fileList.add(files);
            }
            System.out.println(fileList.size());
            System.out.println(fileName);
            if (buiuldCsv.buildCsv(fileList, fileName)) {
             return "true";
            }
        }return  "false";
    }
    }
