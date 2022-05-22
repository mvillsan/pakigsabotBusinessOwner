package com.capstone.pakigsabotbusinessowner.Resort;

public class ResortPromoAndDealsModel {
    String resortPADId;
    String resortPADName;
    String resortPADDesc;
    String resortPADStartDate;
    String resortPADEndDate;

    public ResortPromoAndDealsModel() {
        //empty constructor needed
    }

    public ResortPromoAndDealsModel(String resortPADId, String resortPADName, String resortPADDesc, String resortPADStartDate, String resortPADEndDate) {
        this.resortPADId = resortPADId;
        this.resortPADName = resortPADName;
        this.resortPADDesc = resortPADDesc;
        this.resortPADStartDate = resortPADStartDate;
        this.resortPADEndDate = resortPADEndDate;
    }


    public String getResortPADId() { return resortPADId;
    }

    public void setResortPADId(String resortPADId) { this.resortPADId = resortPADId;
    }

    public String getResortPADName() { return resortPADName;
    }

    public void setResortPADName(String resortPADName) { this.resortPADName = resortPADName;
    }

    public String getResortPADDesc() { return resortPADDesc;
    }

    public void setResortPADDesc(String resortPADDesc) { this.resortPADDesc = resortPADDesc;
    }

    public String getResortPADStartDate() { return resortPADStartDate;
    }

    public void setResortPADStartDate(String resortPADStartDate) { this.resortPADStartDate = resortPADStartDate;
    }

    public String getResortPADEndDate() { return resortPADEndDate;
    }

    public void setResortPADEndDate(String resortPADEndDate) { this.resortPADEndDate = resortPADEndDate;
    }

}
