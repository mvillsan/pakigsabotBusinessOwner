package com.capstone.pakigsabotbusinessowner.Cafe.PromoAndDeals;

public class CafePromoAndDealsModel {
    String cafePADId;
    String cafePADName;
    String cafePADDesc;
    String cafePADStartDate;
    String cafePADEndDate;

    public CafePromoAndDealsModel() {
        //empty constructor needed
    }


    public CafePromoAndDealsModel(String cafePADId, String cafePADName, String cafePADDesc, String cafePADStartDate, String cafePADEndDate) {
        this.cafePADId = cafePADId;
        this.cafePADName = cafePADName;
        this.cafePADDesc = cafePADDesc;
        this.cafePADStartDate = cafePADStartDate;
        this.cafePADEndDate = cafePADEndDate;
    }

    public String getCafePADId() {
        return cafePADId;
    }

    public void setCafePADId(String cafePADId) {
        this.cafePADId = cafePADId;
    }

    public String getCafePADName() {
        return cafePADName;
    }

    public void setCafePADName(String cafePADName) {
        this.cafePADName = cafePADName;
    }

    public String getCafePADDesc() {
        return cafePADDesc;
    }

    public void setCafePADDesc(String cafePADDesc) {
        this.cafePADDesc = cafePADDesc;
    }

    public String getCafePADStartDate() {
        return cafePADStartDate;
    }

    public void setCafePADStartDate(String cafePADStartDate) {
        this.cafePADStartDate = cafePADStartDate;
    }

    public String getCafePADEndDate() {
        return cafePADEndDate;
    }

    public void setCafePADEndDate(String cafePADEndDate) {
        this.cafePADEndDate = cafePADEndDate;
    }



}
