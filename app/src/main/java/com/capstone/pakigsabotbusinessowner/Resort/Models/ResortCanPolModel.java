package com.capstone.pakigsabotbusinessowner.Resort.Models;

//Model
public class ResortCanPolModel {
    String resortCPolID;
    String resortCPolDesc;

    public ResortCanPolModel() {
        //empty constructor needed
    }

    public ResortCanPolModel(String resortCPolID, String resortCPolDesc) {
        this.resortCPolID = resortCPolID;
        this.resortCPolDesc = resortCPolDesc;
    }

    public String getResortCPolID() {
        return resortCPolID;
    }

    public void setResortCPolID(String resortCPolID) {
        this.resortCPolID = resortCPolID;
    }

    public String getResortCPolDesc() {
        return resortCPolDesc;
    }

    public void setResortCPolDesc(String resortCPolDesc) {
        this.resortCPolDesc = resortCPolDesc;
    }
}
