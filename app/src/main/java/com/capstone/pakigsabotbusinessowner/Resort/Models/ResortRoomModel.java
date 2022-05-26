package com.capstone.pakigsabotbusinessowner.Resort.Models;

//Model
public class ResortRoomModel {
    String resortRFID;
    String resortRFName;
    String resortCapac;
    String resortDesc;
    String resortRate;
    String resortImgUrl;
    String estID;

    public ResortRoomModel(){
        //Empty constructor needed
    }

    public ResortRoomModel(String resortRFID, String resortRFName,
                           String resortCapac, String resortDesc, String resortRate, String resortImgUrl, String estID) {
        this.resortRFID = resortRFID;
        this.resortRFName = resortRFName;
        this.resortCapac = resortCapac;
        this.resortDesc = resortDesc;
        this.resortRate = resortRate;
        this.resortImgUrl = resortImgUrl;
        this.estID = estID;
    }

    public String getResortRFID() {
        return resortRFID;
    }

    public void setResortRFID(String resortRFID) {
        this.resortRFID = resortRFID;
    }

    public String getResortRFName() {
        return resortRFName;
    }

    public void setResortRFName(String resortRFName) {
        this.resortRFName = resortRFName;
    }

    public String getResortCapac() {
        return resortCapac;
    }

    public void setResortCapac(String resortCapac) {
        this.resortCapac = resortCapac;
    }

    public String getResortDesc() {
        return resortDesc;
    }

    public void setResortDesc(String resortDesc) {
        this.resortDesc = resortDesc;
    }

    public String getResortRate() {
        return resortRate;
    }

    public void setResortRate(String resortRate) {
        this.resortRate = resortRate;
    }

    public String getResortImgUrl() {
        return resortImgUrl;
    }

    public void setResortImgUrl(String resortImgUrl) {
        this.resortImgUrl = resortImgUrl;
    }

    public String getEstID() {
        return estID;
    }

    public void setEstID(String estID) {
        this.estID = estID;
    }
}