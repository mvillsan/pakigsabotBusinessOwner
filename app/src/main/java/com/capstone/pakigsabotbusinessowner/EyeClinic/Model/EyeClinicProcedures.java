package com.capstone.pakigsabotbusinessowner.EyeClinic.Model;

public class EyeClinicProcedures {

    String opticalPRId, opticalPRName, opticalPRDesc, opticalPRRate, opticalPRImgUrl;

    //required empty constructor
    public EyeClinicProcedures() {
    }

    //constructor
    public EyeClinicProcedures(String opticalPRId, String opticalPRName, String opticalPRDesc, String opticalPRRate, String opticalPRImgUrl) {
        this.opticalPRId = opticalPRId;
        this.opticalPRName = opticalPRName;
        this.opticalPRDesc = opticalPRDesc;
        this.opticalPRRate = opticalPRRate;
        this.opticalPRImgUrl = opticalPRImgUrl;
    }

    //required value for fetching the data from Firestore
    public String getOpticalPRId() { return opticalPRId;
    }

    public void setOpticalPRId(String opticalPRId) { this.opticalPRId = opticalPRId;
    }

    public String getOpticalPRName() { return opticalPRName;
    }

    public void setOpticalPRName(String opticalPRName) { this.opticalPRName = opticalPRName;
    }

    public String getOpticalPRDesc() { return opticalPRDesc;
    }

    public void setOpticalPRDesc(String opticalPRDesc) { this.opticalPRDesc = opticalPRDesc;
    }

    public String getOpticalPRRate() { return opticalPRRate;
    }

    public void setOpticalPRRate(String opticalPRRate) { this.opticalPRRate = opticalPRRate;
    }

    public String getOpticalPRImgUrl() { return opticalPRImgUrl;
    }

    public void setOpticalPRImgUrl(String opticalPRImgUrl) { this.opticalPRImgUrl = opticalPRImgUrl;
    }




}
