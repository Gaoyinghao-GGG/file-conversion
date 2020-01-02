package com.iocasckani.project.file_conversion.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ADCPDispose {

    public static List<String> readDataHeader(MultipartFile mFile) throws IOException {
        InputStream fileStream = mFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
        String data = null;
        List<String> list = new ArrayList<String>();
        // 读取每行的内容
        while ((data = br.readLine()) != null) {
            if (data.length()>3 && data.substring(0,3).equals("050")) {
                list.add(data);
                }
        }
        return list;
    }
    public static List<Map<String, Object>> ReadCTdDataContent(MultipartFile mFile) {
        try {
            InputStream fileStream = mFile.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
            //根据需要导入的表来定义具体的字段
            List<Map<String, Object>> subsurfBuoyAdcp = new ArrayList<Map<String, Object>>();
            String data = null;
            int order = 0;
            int siz = 0;
            while ((data = br.readLine()) != null) {
                System.out.println(siz);
                Map<String, Object> map = new HashMap<String, Object>();
                if(data.contains("WPO-PAC")){continue;}
                if (data.length()>3 && data.substring(0,3).equals("050")) {
                    order++;
                  continue;
                }
                if(data.contains("$STOP")){break;}
                String[] res = data.trim().split("\\s+");
                for(int i = 0;i< res.length;i++){
                    if(res[i].equals("") || res[i].equals("NaN")){res[i] = "NaN";}
                }
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("order",order);
                mapTemp.put("depth",res[0]);
                //System.out.println(mapTemp.get("depth"));
                mapTemp.put("flow_velocity",res[2]);
                mapTemp.put("flow_direction",res[3]);
                mapTemp.put("zonal_flow",res[4]);
                mapTemp.put("meridional_flow",res[5]);
                mapTemp.put("PG",res[6]);
                subsurfBuoyAdcp.add(mapTemp);
            }
            return subsurfBuoyAdcp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
