package com.iocasckani.project.file_conversion.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CTDCnvDispose {

    public static List<String> readDataHeader(MultipartFile mFile) throws IOException {
        InputStream fileStream = mFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
        String data = null;
        List<String> list = new ArrayList<String>();
        // 读取每行的内容
        while ((data = br.readLine()) != null) {
            if (data.contains("# name")) {
                String name = null;
                name = data.split("=")[1].trim().split(":")[0];
                if (name.equals("timeJV2") || name.equals("timeK") || name.equals("timeM") || name.equals("timeH") || name.equals("timeJ") || name.equals("flag")
                        || name.equals("prdM")) {
                    name = "";
                }
                for (String s : list) {
                    if (s.equals(name)) {
                        name = "";
                    }
                }
                list.add(name);
            }
            if (data.equals("*END*")) {
                return list;
            }
        }
        return list;
    }

    public static List<Map<String, Object>> ReadCTdDataContent(MultipartFile mFile, List<String> list, String depth) {
        try {
            InputStream fileStream = mFile.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
            //根据需要导入的表来定义具体的字段
            List<Map<String, Object>> subsurfBuoyAdcp = new ArrayList<Map<String, Object>>();
            String data = null;
            String datetime = null;
            String longitude = null;
            String latitude = null;
            boolean isData = false;
            while ((data = br.readLine()) != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (data.equals("*END*")) {
                    isData = true;
                    continue;
                }
                if (!isData) {
                    if (data.contains("# start_time")) {
                        String Strdate = data.split("=")[1].trim();
                        Strdate = Strdate.split("\\[")[0].trim();
                        SimpleDateFormat sdata = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
                        Date datee = null;
                        try {
                            datee = sdata.parse(Strdate);
                        } catch (ParseException e) {
                            System.out.println("时间异常");
                            e.printStackTrace();
                        }
                        SimpleDateFormat sdatb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        datetime = sdatb.format(datee);
                    }
                    if (data.contains("** Latitude")) {
                        System.out.println("得到lat");
                        latitude = data.split(":")[1].trim().replaceAll("[a-zA-Z]", "");
                    }
                    if (data.contains("** Longitude")) {
                        longitude = data.split(":")[1].trim().replaceAll("[a-zA-Z]", "");
                    }
                    continue;
                }
                data = data.trim().replaceAll(" +", " ");
                String[] res = data.split(" ");
                int ss = 0;
                String temperature = null;
                String salinity = null;
                for (String s : list) {
                    if (s.equals("tv290C")) {
                        temperature = res[ss];
                    }
                    if (s.equals("sal00")) {
                        salinity = res[ss];
                    }
                    ss++;
                }
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("longitude", longitude);
                mapTemp.put("latitude", latitude);
                //mapTemp.put("datetime", datetime);
                mapTemp.put("depth", depth);
                mapTemp.put("temperature", temperature);
                mapTemp.put("salinity", salinity);
                for (int i = 0; i < list.size(); i++) {
                    String name = list.get(i);
                    if (name.equals("depSM") || name.equals("t090C") || name.equals("sal00")) {
                        continue;
                    }
                    if (name.equals("")) {
                        continue;
                    }
                    if (name.equals("timeS")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime));
                        long millisecond = calendar.getTimeInMillis() + Long.parseLong(Float.valueOf(Float.parseFloat(res[i]) * 1000).longValue() + "");
                        Date date = new Date(millisecond);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        mapTemp.put("datetime", format.format(date));
                        continue;
                    }
                    mapTemp.put(name, res[i]);
                }
                subsurfBuoyAdcp.add(mapTemp);
            }
            return subsurfBuoyAdcp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取表头介绍
    public static List<String> ReadIntroduce(MultipartFile mFile) throws IOException {
        List<String> list = new ArrayList<>();
        InputStream fileStream = mFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
        String data = null;
        // 读取每行的内容
        while ((data = br.readLine()) != null) {
            if (data.contains("* Sea-Bird")) {
                list.add(data);
            }
            if (data.contains("* SBE")) {
                list.add(data);
            }
            if (data.contains("* System UpLoad")) {
                list.add(data);
            }
        }
        return list;
    }
}
