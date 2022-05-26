package com.capstone.pakigsabotbusinessowner.Upload;

public class UploadResortImgDetails {
    String resortImgUrl;
    String resortRFName;
    int resortCapac;
    String resortDesc;
    double resortRate;

    public UploadResortImgDetails(){
        //Empty constructor needed
    }

    public UploadResortImgDetails(String url, String name, int capacity, String desc, double rate){
        if(name.trim().equals("")){
            name = "Image";
        }
        resortImgUrl = url;
        resortRFName = name;
        resortCapac = capacity;
        resortDesc = desc;
        resortRate = rate;
    }

    public String getResortImgUrl(){
        return resortImgUrl;
    }

    public void setResortImgUrl(String url){
        resortImgUrl = url;
    }

    public String getResortRFName() {
        return resortRFName;
    }

    public void setResortRFName(String name) {
        this.resortRFName = name;
    }

    public int getResortCapac() {
        return resortCapac;
    }

    public void setResortCapac(int capacity) {
        this.resortCapac = capacity;
    }

    public String getResortDesc() {
        return resortDesc;
    }

    public void setResortDesc(String desc) {
        this.resortDesc = desc;
    }

    public double getResortRate() {
        return resortRate;
    }

    public void setResortRate(double rate) {
        this.resortRate = rate;
    }
}