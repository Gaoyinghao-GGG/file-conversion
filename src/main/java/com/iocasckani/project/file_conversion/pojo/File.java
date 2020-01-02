package com.iocasckani.project.file_conversion.pojo;

public class File {
    private String longitude;

    private String latitude;

    private String datetime;

    private String temperature;

    private String depth;

    private String salinity;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getSalinity() {
        return salinity;
    }

    public void setSalinity(String salinity) {
        this.salinity = salinity;
    }

    public File(String longitude, String latitude, String datetime, String temperature, String depth, String salinity) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.datetime = datetime;
        this.temperature = temperature;
        this.depth = depth;
        this.salinity = salinity;
    }
    public File(){}

    public String dataFormat(){
        return String.format("%s,%s,%s,%s,%s,%s",this.longitude,this.latitude,this.datetime,this.depth,this.temperature,
                this.salinity);
    }
}
