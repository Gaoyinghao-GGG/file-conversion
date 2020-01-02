package com.iocasckani.project.file_conversion.util.merge;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class quanqiu {

    public static List<String> readDataHeader(MultipartFile mFile) throws IOException {
        System.out.println("获得头");
        InputStream fileStream = mFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
        String data = null;
        List<String> list = new ArrayList<String>();
        // 读取每行的内容
        while ((data = br.readLine()) != null) {
            if (data.contains("# name")) {
               list.add(data.trim());
            }
            if (data.equals("*END*")) {
                return list;
            }
        }
        return list;
    }

    public static List<Map<String,Object>> CNVDispose(MultipartFile mFile)throws IOException {
        List<String> list = new ArrayList<>();
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
        InputStream fileStream = mFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
        String data = null;
        List<Map<String, Object>> subsurfBuoyCNV = new ArrayList<Map<String, Object>>();
        boolean isData = false;
        String datetime = null;
        String longitude = null;
        String latitude = null;
        List<String> headList = readDataHeader(mFile);//name信息
        // 读取每行的内容
        while ((data = br.readLine()) != null) {
            if (data.trim().equals("*END*")) {
                isData = true;
                continue;
            }
            if(!isData){
                if (data.contains("* System UpLoad Time")) {
                    String Strdate = data.split("=")[1].trim();
                    SimpleDateFormat sdata = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
                    Date datee = null;
                    try {
                        datee = sdata.parse(Strdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdatb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    datetime = sdatb.format(datee);
                }
                else if (data.contains("* NMEA Latitude")) {
                    String unlatitude = data.split("=")[1].trim().replaceAll("[a-zA-Z]", "").trim();
                    float latfen = Float.parseFloat(unlatitude.split("\\s+")[1])/60;
                    float latdu = Float.parseFloat(unlatitude.split("\\s+")[0]);
                    latitude = String.valueOf(latdu+latfen);
                }
                else if ( data.contains("** Lat")){
                    String unlatitude = data.split(":")[1].trim().replaceAll("[a-zA-Z]", "").trim();
                    float latfen = Float.parseFloat(unlatitude.split("\\s+")[1])/60;
                    float latdu = Float.parseFloat(unlatitude.split("\\s+")[0]);
                    latitude = String.valueOf(latdu+latfen);
                }
                else if (data.contains("* NMEA Longitude")) {
                    String unlongitude = data.split("=")[1].trim().replaceAll("[a-zA-Z]", "").trim();
                    float lonfen = Float.parseFloat(unlongitude.split("\\s+")[1])/60;
                    float londu = Float.parseFloat(unlongitude.split("\\s+")[0]);
                    longitude = String.valueOf(londu+lonfen);
                }
                else if ( data.contains("** Lon")){
                    String unlongitude = data.split(":")[1].trim().replaceAll("[a-zA-Z]", "").trim();
                    float lonfen = Float.parseFloat(unlongitude.split("\\s+")[1])/60;
                    float londu = Float.parseFloat(unlongitude.split("\\s+")[0]);
                    longitude = String.valueOf(londu+lonfen);
                }
                continue;
            }
            Map<String , Object> map = new HashMap<>();
            if(data == null || data.equals("") || data.equals(" ")){continue;}
            String[] datalist = data.split("\\s+");
            map.put("lat",latitude);
            map.put("lon", longitude);
            map.put("datetime", datetime);
            String newStr =null;
            for (String ss:list) {
                int i = 0;
                for (String head:headList) {
                    i++;
                    if(ss.equals("ph")){newStr = ss;}
                    else if(ss.equals("sound velocity") || ss.equals("potential temperature")){
                        String sound = ss.split(" ")[0].trim();
                        String velocity = ss.split(" ")[1].trim();
                        String newstr1 =  sound.substring(0, 1).toUpperCase() + sound.substring(1);
                        String newstr2 =  velocity.substring(0, 1).toUpperCase() + velocity.substring(1);
                        newStr = newstr1 + " " + newstr2;
                    }
                    else{
                        newStr = ss.substring(0, 1).toUpperCase() + ss.substring(1);}
                    if(head.contains(newStr)){
                        System.out.println(newStr);
                        map.put(ss,datalist[i]);
                        break;
                    }
                }
            }

            /*String[] datalist = data.split("\\s+");
            map.put("longitude", longitude);
            map.put("latitude",latitude);
            map.put("datetime", datetime);
            map.put("depth",datalist[21]);
            map.put("temperature",datalist[2]);
            map.put("salinity",datalist[5]);
            map.put("conductivity",datalist[3]);
            map.put("turbidity",datalist[6]);
            map.put("pressure",datalist[1]);
            map.put("ph",datalist[9]);
            map.put("density",datalist[18]);
            map.put("sound velocity",datalist[20]);
            map.put("Oxygen",datalist[8]);*/

            subsurfBuoyCNV.add(map);
        }
        return subsurfBuoyCNV;
    }

}
