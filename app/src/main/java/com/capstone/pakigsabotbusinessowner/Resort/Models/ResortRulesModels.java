package com.capstone.pakigsabotbusinessowner.Resort.Models;

//Model
public class ResortRulesModels {
    String resortRulesID;
    String resortRulesDesc;

    public ResortRulesModels() {
        //empty constructor needed
    }

    public ResortRulesModels(String resortRulesID, String resortRulesDesc) {
        this.resortRulesID = resortRulesID;
        this.resortRulesDesc = resortRulesDesc;
    }

    public String getResortRulesID() {
        return resortRulesID;
    }

    public void setResortRulesID(String resortRulesID) {
        this.resortRulesID = resortRulesID;
    }

    public String getResortRulesDesc() {
        return resortRulesDesc;
    }

    public void setResortRulesDesc(String resortRulesDesc) {
        this.resortRulesDesc = resortRulesDesc;
    }
}
