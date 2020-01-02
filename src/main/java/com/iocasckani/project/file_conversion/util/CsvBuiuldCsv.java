package com.iocasckani.project.file_conversion.util;

import com.iocasckani.project.file_conversion.pojo.File;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class CsvBuiuldCsv {
    File file;

    public boolean buildCsv(List<File> fileList, String filename) {
        if (fileList != null && fileList.size() > 0) {
            System.out.println("进入方法");
            String[] headArr = new String[]{"longitude", "latitude", "datetime", "depth", "temperature", "salinity"};
            List<Object> headList = Arrays.asList(headArr);
            //String filePath = "C:\\Users\\18487\\Desktop\\csv"; //CSV文件路径
            String filePath = "../file/static/file/";
            java.io.File csvfile = null;
            BufferedWriter csvWriter = null;
            try {
                csvfile = new java.io.File(filePath + java.io.File.separator + filename);
                java.io.File parent = csvfile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                csvfile.createNewFile();

                csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvfile), "utf-8"), 1024);
                System.out.println("在目录上生成");
                csvWriter.write("CTD DATA");
                csvWriter.newLine();
                csvWriter.newLine();
                csvWriter.write(String.join(",", headArr));
                //writeRow(headList,csvWriter);
                csvWriter.newLine();

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

    public boolean buildFolderCsv(List<File> fileList, String filename, String filePath) {
        if (fileList != null && fileList.size() > 0) {
            System.out.println("进入方法");
            String[] headArr = new String[]{"longitude", "latitude", "datetime", "depth", "temperature", "salinity"};
            List<Object> headList = Arrays.asList(headArr);
            filePath = "../file/static/file/" + filePath;
            java.io.File csvfile = null;
            BufferedWriter csvWriter = null;
            try {
                csvfile = new java.io.File(filePath + java.io.File.separator + filename);
                java.io.File parent = csvfile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                csvfile.createNewFile();

                csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvfile), "utf-8"), 1024);
                System.out.println("在目录上生成");
                csvWriter.write("CTD DATA");
                csvWriter.newLine();
                csvWriter.newLine();
                csvWriter.write(String.join(",", headArr));
                //writeRow(headList,csvWriter);
                csvWriter.newLine();

                for (File datafile : fileList) {
                    csvWriter.write(datafile.dataFormat());
                    csvWriter.newLine();
                }
                csvWriter.flush();
                System.out.println("完成");
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

    /**
     * 92      * 写一行数据
     * 93      * @param row 数据列表
     * 94      * @param csvWriter
     * 95      * @throws IOException
     * 96
     */
    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
}
