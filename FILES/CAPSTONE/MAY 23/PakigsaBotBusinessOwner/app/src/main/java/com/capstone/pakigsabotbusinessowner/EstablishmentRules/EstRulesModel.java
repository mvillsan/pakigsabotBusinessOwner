package com.capstone.pakigsabotbusinessowner.EstablishmentRules;

public class EstRulesModel {

    String estRulesId;
    String estRulesName;
    String estRulesDesc;
    String estId;

    public EstRulesModel() {
        //empty constructor needed
    }

    public EstRulesModel(String estRulesId, String estRulesName, String estRulesDesc, String estId) {
        this.estRulesId = estRulesId;
        this.estRulesName = estRulesName;
        this.estRulesDesc = estRulesDesc;
        this.estId = estId;
    }

    public String getEstRulesId() {
        return estRulesId;
    }

    public void setEstRulesId(String estRulesId) {
        this.estRulesId = estRulesId;
    }

    public String getEstRulesName() {
        return estRulesName;
    }

    public void setEstRulesName(String estRulesName) {
        this.estRulesName = estRulesName;
    }

    public String getEstRulesDesc() {
        return estRulesDesc;
    }

    public void setEstRulesDesc(String estRulesDesc) {
        this.estRulesDesc = estRulesDesc;
    }

    public String getEstId() {
        return estId;
    }

    public void setEstId(String estId) {
        this.estId = estId;
    }

}
