package com.iocasckani.project.file_conversion.controller.merge;

import com.iocasckani.project.file_conversion.util.CnvBuiuldCsv;
import com.iocasckani.project.file_conversion.util.CnvDispose;
import com.iocasckani.project.file_conversion.util.ZipFiles;
import com.iocasckani.project.file_conversion.util.merge.MergeCnvBuildCsv;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@Controller
public class MergeCnvController {
    CnvBuiuldCsv cnvBuiuldCsv = new CnvBuiuldCsv();
    ZipFiles zipFiles = new ZipFiles();
    CnvDispose cnvDispose = new CnvDispose();

    //合并转化cnv文件夹
    @RequestMapping("/mergeFolderCnv")
    @ResponseBody
    public String importFolderCnv(HttpServletRequest request,int i) throws IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                request.setCharacterEncoding("utf-8");
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

                Iterator iter = multipartRequest.getFileNames();

                MultipartFile mfile = multipartRequest.getFile(iter.next().toString());
                // 读取文件名称：按文件名称、站号进行区分，获取对应的站位ID
                String fileName = mfile.getOriginalFilename();

                fileName = fileName.split("/")[0]+".csv";
                String filePath = "../file/static/file/";
                List<String> DataHeader = CnvDispose.readDataHeader(mfile);
                List<Map<String, Object>> mapList = CnvDispose.ReadDataContent(mfile,DataHeader);
                List<String> Introduce = cnvDispose.ReadIntroduce(mfile);
                if(i==0){cnvBuiuldCsv.buildCsv(DataHeader, mapList, fileName,filePath,Introduce);}
                else {
                    MergeCnvBuildCsv.buildMergeCsv(DataHeader, mapList, fileName,filePath,Introduce); }
                    return "true";
            }catch (Exception ex) {
                return "false";
            }
        }
       return "false";
    }
}
