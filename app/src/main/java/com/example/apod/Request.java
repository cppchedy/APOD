package com.example.apod;

public class Request {
    private String baseUrl;
    private String service;
    private String args;
    private int code;

    public Request(String bUrl) {
        this.baseUrl = bUrl;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return baseUrl + ";" + service + ";" + args + ";" + code;
    }
}
