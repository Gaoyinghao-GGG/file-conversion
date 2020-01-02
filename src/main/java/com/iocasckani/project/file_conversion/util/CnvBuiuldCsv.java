package com.iocasckani.project.file_conversion.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CnvBuiuldCsv {
    public boolean buildCsv(List<String> list, List<Map<String, Object>> mapList, String fileName, String filePath, List<String> interoduce) {
        System.out.println("进入方法");
        String f = fileName.toString();
        String filename = f.substring(0, f.indexOf(".")) + ".csv";
        if (mapList != null && mapList.size() > 0) {
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
                //list中去空值
                list.add(0, "latitude");
                list.add(1, "longitude");
                list.add(2, "datetime");
                list.add(3, "depth");
                list.add(4, "temperature");
                list.add(5, "salinity");
                for (String s : interoduce) {
                    csvWriter.write(s);
                    csvWriter.newLine();
                }
                csvWriter.newLine();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals("timeS")) {
                        list.set(i, "");
                    }
                    if (list.get(i).equals("depSM") || list.get(i).equals("t090C") || list.get(i).equals("sal00")) {
                        list.set(i, "");
                    }
                    if (list.get(i).equals("")) {
                        list.set(i, "");
                    }
                }
                csvWriter.write(String.join(",", changeList(list)));
                csvWriter.newLine();
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> m = mapList.get(i);
                    if (i != 0) {
                        String last = mapList.get(i - 1).get("datetime").toString();
                        String next = m.get("datetime").toString();
                        String hist = null;
                        for (int j = 0; j < i; j++) {
                            String hist1 = mapList.get(j).get("datetime").toString();
                            if (next.equals(hist1)) {
                                hist = hist1;
                                break;
                            }
                        }
                        if (hist != null) {
                            continue;
                        }
                        /*if(last.equals(next)){
                            continue;
                        }*/
                    }
                    for (String s : list) {
                        if (s.equals("")) {
                            continue;
                        }
                        csvWriter.write(m.get(s).toString() + ",");
                    }
                    csvWriter.newLine();
                }
                return true;
            } catch (Exception e) {
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
        return false;
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

    private static List changeList(List<String> list) {
        List<String> newlist = new ArrayList<String>();
        for (String s : list) {
            if (!s.equals("")) {
                newlist.add(s);
            }
        }
        return newlist;
    }
}
