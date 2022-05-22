package com.capstone.pakigsabotbusinessowner.EyeClinic.PromoAndDeals;

public class EyeClinicPromoAndDealsModel {

    String opticalPADId;
    String opticalPADName;
    String opticalPADDesc;
    String opticalPADStartDate;
    String opticalPADEndDate;

    public EyeClinicPromoAndDealsModel() {
        //empty constructor needed
    }

    public EyeClinicPromoAndDealsModel(String opticalPADId, String opticalPADName, String opticalPADDesc, String opticalPADStartDate, String opticalPADEndDate) {
        this.opticalPADId = opticalPADId;
        this.opticalPADName = opticalPADName;
        this.opticalPADDesc = opticalPADDesc;
        this.opticalPADStartDate = opticalPADStartDate;
        this.opticalPADEndDate = opticalPADEndDate;
    }

    public String getOpticalPADId() { return opticalPADId;
    }

    public void setOpticalPADId(String opticalPADId) { this.opticalPADId = opticalPADId;
    }

    public String getOpticalPADName() { return opticalPADName;
    }

    public void setOpticalPADName(String opticalPADName) { this.opticalPADName = opticalPADName;
    }
    public String getOpticalPADDesc() { return opticalPADDesc;
    }

    public void setOpticalPADDesc(String opticalPADDesc) { this.opticalPADDesc = opticalPADDesc;
    }

    public String getOpticalPADStartDate() { return opticalPADStartDate;
    }

    public void setOpticalPADStartDate(String opticalPADStartDate) { this.opticalPADStartDate = opticalPADStartDate;
    }

    public String getOpticalPADEndDate() { return opticalPADEndDate;
    }

    public void setOpticalPADEndDate(String opticalPADEndDate) { this.opticalPADEndDate = opticalPADEndDate;
    }
}
