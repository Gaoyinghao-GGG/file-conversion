package com.iocasckani.project.file_conversion.controller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class CrateNCML {

   /* public static void main(String[] args) {
        int start = 2010;
        int end = 2010;
        String type = null;
        String[] types ={"sfc"};
        for (String str:types) {
            type = "hycom_glb_"+str;
            createXml(start,end,"glb_regp05_");
        }
    }*/

    public static void createXml(int start, int end, String type) {
        try {
            // 创建document对象
            Document document = DocumentHelper.createDocument();
            // 创建节点  添加xmlns属性
            Element netcdf = document.addElement("netcdf", "http://www.unidata.ucar.edu/namespaces/netcdf/ncml-2.2");

            Element aggregation = netcdf.addElement("aggregation");
            aggregation.addAttribute("dimName", "time");
            aggregation.addAttribute("type", "joinExisting");
            int truestart = start;
            //创建年份
            for (int i = start; start <= end; start++) {
                //for(int y = 1;y<13;y++) {
                Element scan = aggregation.addElement("scan");
                //scan.addAttribute("location", "../" + start +String.format("%02d",y));
                //scan.addAttribute("location", "../" + start+".*"); NCOM_catalog_ncom_Region5
                scan.addAttribute("location", "..");
                //scan.addAttribute("regExp", "ncom_glb_regp05_2010050900.nc");
                //scan.addAttribute("regExp", ".*" + type + ".*.nc$");
                scan.addAttribute("regExp", ".*" + type + "2010" + ".*.nc$");
                scan.addAttribute("subirs", "false");
                // }
            }
            // 设置生成文件的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");

            // 生成文件
            //File file = new File("C:\\Users\\18487\\Desktop\\aggregation_"+type+"_"+truestart+"-"+end+".ncml");
            File file = new File("C:\\Users\\18487\\Desktop\\aggregation_" + type + truestart + ".ncml");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("生成aggregation_" + type + "_" + truestart + "-" + end + ".ncml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成test.ncml失败");
        }
    }
}
