package com.iocasckani.project.file_conversion.util.merge;

import com.iocasckani.project.file_conversion.pojo.File;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MergeCsvBuildCsv {

    public static boolean buildCsv(List<File> fileList, String filename) {
        if (fileList != null && fileList.size() > 0) {
            System.out.println("进入方法");

            String filePath = "../file/static/file/";
            java.io.File csvfile = null;
            BufferedWriter csvWriter = null;
            try {
                csvfile = new java.io.File(filePath + java.io.File.separator + filename);
                csvWriter = new BufferedWriter(new FileWriter(csvfile, true));

                for (File datafile : fileList) {
                    csvWriter.write(datafile.dataFormat());
                    csvWriter.newLine();
                }
                csvWriter.flush();
            } catch (Exception e) {
                System.out.println("抛出异常");
                e.printStackTrace();
            } finally {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
