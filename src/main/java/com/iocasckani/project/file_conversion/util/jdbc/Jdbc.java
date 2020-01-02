package com.iocasckani.project.file_conversion.util.jdbc;

import java.sql.*;

public class Jdbc {

    public static ResultSet LinkJDBC(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xddbmain", "root", "ok");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            System.out.println("数据库搜索出错");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("未找到驱动");
            e.printStackTrace();
        }
        return null;
    }
}
