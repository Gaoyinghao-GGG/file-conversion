package com.iocasckani.project.file_conversion.util;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvDispose {

    public Map<String, Object> fileDispose(CsvReader csvReader) throws IOException {
        String date = csvReader.getValues()[2];
        SimpleDateFormat sdata = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        Date datee = null;
        try {
            datee = sdata.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdatb = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        date = sdatb.format(datee);
        String time = csvReader.getValues()[3];
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("longitude", csvReader.getValues()[0].replaceAll("[a-zA-Z]", ""));
        map.put("latitude", csvReader.getValues()[1].replaceAll("[a-zA-Z]", ""));
        map.put("datetime", date + " " + time);
        map.put("depth", csvReader.getValues()[4]);
        map.put("temperature", csvReader.getValues()[5]);
        map.put("salinity", csvReader.getValues()[7]);
        return map;
    }
}
