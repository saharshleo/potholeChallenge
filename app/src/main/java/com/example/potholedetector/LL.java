package com.example.potholedetector;

class LL {
    public String lat, log;
    public int i;
    public LL(String lat, String log, int i) {
        this.lat = lat;
        this.log = log;
        this.i = i;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
