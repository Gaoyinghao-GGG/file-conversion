package com.iocasckani.project.file_conversion.util;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ADCPBuiuldCsv {

    public static boolean BuildADCPCsv(MultipartFile mFile,String fileName, String filePath) throws IOException {
        java.io.File csvfile = null;
        BufferedWriter csvWriter = null;
        InputStream fileStream = mFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
        try {
            String f = fileName.toString();
            String filename = f.substring(0,f.indexOf("."))+".csv";

            csvfile = new java.io.File(filePath + java.io.File.separator + filename);
            java.io.File parent = csvfile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvfile.createNewFile();
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvfile), "utf-8"), 1024);
            System.out.println("在目录上生成");

            String data = null;
            List<String> headList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            list.add("latitude");
            list.add("longitude");
            list.add("datetime");
            list.add("depth");
            list.add("flow_velocity");
            list.add("flow_direction");
            csvWriter.write(String.join(",", list));
            csvWriter.newLine();
            int s = 0;
            while ((data = br.readLine())!=null){
                if(data.contains("WPO-PAC")){continue;}
                if(data.contains("$STOP")){break;}
                if (data.length()>3 && data.substring(0,3).equals("050")) {
                    headList.add(data);
                    s++;
                    System.out.println(s);
                    continue; }
                int siz = headList.size();
                String [] head = headList.get(siz -1).split(" ");
                String [] body = data.trim().split("\\s+");
                   /* for(int i =0;i< body.length;i++){
                        System.out.println(body[i]);
                    }*/
                //开始写入
                String lat = head[5]; int latlen = lat.length();
                String latitude = lat.substring(0,2)+"."+lat.substring(2,latlen);//经度
                csvWriter.write(latitude+",");

                String lon = head[7]; int lonlen = lon.length();
                String longitude = lon.substring(0,3)+"."+lon.substring(3,lonlen);
                csvWriter.write(longitude+",");//纬度

                String datetime = null;
                String date = head[3]+" "+head[4];//时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.ENGLISH);
                Date datee = null;
                try {
                    datee = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdatb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                datetime = sdatb.format(datee);
                csvWriter.write(datetime+",");
                //深度
                csvWriter.write(body[0]+",");
               // csvWriter.write(head[12].toString()+",");//声学换能器水温
                //csvWriter.write(head[10].toString()+",");//航向
                csvWriter.write(body[2].toString()+",");//流速
                csvWriter.write(body[3].toString()+",");//流向
               //csvWriter.write(body[4].toString()+",");//纬向流
                //csvWriter.write(body[5].toString()+",");//经向流
                //csvWriter.write(body[6].toString()+",");//PG

                csvWriter.newLine();
            }
            return true;
        }catch (Exception ex) {
            System.out.println("抛出异常");
            ex.printStackTrace();
            return false;
        }finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }

