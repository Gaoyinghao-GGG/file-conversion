package com.iocasckani.project.file_conversion.util.merge;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class quanqiuBuild {

    public static  boolean buildCsv(List<Map<String, Object>> mapList, String fileName, String filePath){
        System.out.println("进入生成方法");
        String f = fileName.toString();
        String filename = f.substring(0,f.lastIndexOf("."))+".csv";
        filename = "scienceTechnologyWork(2014)"+filename;
        List<String> list = new ArrayList<>();
        list.add("lat");
        list.add("lon");
        list.add("datetime");
        list.add("depth");
        list.add("temperature");
        list.add("salinity");
        list.add("conductivity");
        list.add("turbidity");
        list.add("pressure");
        list.add("ph");
        list.add("density");
        list.add("sound velocity");
        list.add("Oxygen");

        if(mapList != null && mapList.size()>0){
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
                csvWriter.newLine();
                csvWriter.write(String.join(",", list));
                csvWriter.newLine();
                for (Map<String, Object> m : mapList){
                    for (String s:list) {
                        csvWriter.write(m.get(s)+",");
                    }
                    csvWriter.newLine();
                }
                return true;
            }catch (Exception e) {
                System.out.println("抛出异常");
                e.printStackTrace();
                return false;
            } finally {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }
}
