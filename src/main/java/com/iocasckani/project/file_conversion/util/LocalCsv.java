package com.iocasckani.project.file_conversion.util;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class LocalCsv {

    public  static boolean WriteCsv( List<Map<String, Object>> FileData){
        String longitude = null;
        String latitude = null;
        String datetime  = null;
        String depth = null;
        String temperature = null;
        String salinity = null;
        for (Map<String, Object> map:FileData){
            longitude = map.get("longitude").toString();
            latitude = map.get("latitude").toString();
            datetime = map.get("datetime").toString();
            depth = map.get("depth").toString();
            temperature = map.get("temperature").toString();
            salinity = map.get("salinity").toString();
            String sql = "INSERT INTO csv VALUES('"+longitude+"','"+latitude+"','"+datetime+"','"+depth+"','"+temperature+"','"+salinity+"')";
            ResultSet rs = LinkJDBC(sql);
        }

        return true;
    }

    public static ResultSet LinkJDBC(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "ok");
            stmt = conn.createStatement();
            //rs = stmt.executeQuery(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("数据库搜索出错");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("未找到驱动");
            e.printStackTrace();
        }finally{
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {

                    stmt.close();

                }
                if (conn != null) {

                    conn.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
