package com.example.potholedetector;

public class FeedBack {
    String feed,city;

    public FeedBack(String feed, String city) {
        this.feed = feed;
        this.city = city;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
