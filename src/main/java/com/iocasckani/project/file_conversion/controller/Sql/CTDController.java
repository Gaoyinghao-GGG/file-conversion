package com.iocasckani.project.file_conversion.controller.Sql;

import com.iocasckani.project.file_conversion.util.jdbc.Jdbc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CTDController {

    @RequestMapping("/SqlCTD")
    public static String CtdConversion(){

        String selectSummary = "select * from CTDSummary";
        String selectData = "select * from CTDData";
        String selectStationID = "select * from CTDSummary group by StationID";
        //String selectCTDByStation = "select * from CTDData where StationID =";
        ResultSet stationId = Jdbc.LinkJDBC(selectStationID);
        List<Object> stationList = new ArrayList<>();//站位集合
        try {
            while(stationId.next()){stationList.add(stationId.getObject(5));}
            for (Object ob:stationList) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;
    }
}
