package com.iocasckani.project.file_conversion.controller.merge;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.iocasckani.project.file_conversion.pojo.File;
import com.iocasckani.project.file_conversion.util.CsvBuiuldCsv;
import com.iocasckani.project.file_conversion.util.CsvDispose;
import com.iocasckani.project.file_conversion.util.LocalCsv;
import com.iocasckani.project.file_conversion.util.ZipFiles;
import com.iocasckani.project.file_conversion.util.merge.MergeCsvBuildCsv;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class MergeCsvController {
    CsvBuiuldCsv buiuldCsv = new CsvBuiuldCsv();
    CsvDispose fileDispose = new CsvDispose();
    ZipFiles zipFiles = new ZipFiles();


    @RequestMapping("/mergeCsvFolder")
    @ResponseBody
    public String MergeFolder(@ApiParam(value = "csv", required = true) MultipartFile file, HttpServletRequest httpServletRequest, int i) throws IOException {
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

        fileHeadPath = fileName.substring(0, fileName.indexOf("/")) + ".csv";
        System.out.println(fileHeadPath);
        if (i == 0) {
            buiuldCsv.buildCsv(fileList, fileHeadPath); }
        else {
            MergeCsvBuildCsv.buildCsv(fileList, fileHeadPath);
        }
       /* String filePath = fileName.substring(0,fileName.lastIndexOf("/")+1);
        fileName = fileName.split("/")[fileName.split("/").length-1];
        System.out.println(fileName);
        buiuldCsv.buildFolderCsv(fileList, fileName, filePath);*/

        return "true";
    }
}
