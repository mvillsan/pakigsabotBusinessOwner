package com.example.pakigsabot.Clinics.DentalClinic.Models;

import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicProceduresModel;

public class DentalProceduresModel extends DentalClinicProceduresModel {
    String dentalPRId;
    String dentalPRName;
    String dentalPRDesc;
    String dentalPRRate;
    String dentalPRImgUrl;

    public DentalProceduresModel(String dentalPRRId, String dentalPRName, String dentalPRDesc, String dentalPRRate, String dentalPRImgUrl, String estId) {
        //empty constructor needed
    }

    public DentalProceduresModel(String dentalPRId, String dentalPRName, String dentalPRDesc, String dentalPRRate, String dentalPRImgUrl) {
        this.dentalPRId = dentalPRId;
        this.dentalPRName = dentalPRName;
        this.dentalPRDesc = dentalPRDesc;
        this.dentalPRRate = dentalPRRate;
        this.dentalPRImgUrl = dentalPRImgUrl;
    }

    public String getDentalPRId() { return dentalPRId;
    }

    public void setDentalPRId(String dentalPRId) { this.dentalPRId = dentalPRId;
    }

    public String getDentalPRName() { return dentalPRName;
    }

    public void setDentalPRName(String dentalPRName) { this.dentalPRName = dentalPRName;
    }

    public String getDentalPRDesc() { return dentalPRDesc;
    }

    public void setDentalPRDesc(String dentalPRDesc) { this.dentalPRDesc = dentalPRDesc;
    }

    public String getDentalPRRate() { return dentalPRRate;
    }

    public void setDentalPRRate(String dentalPRRate) { this.dentalPRRate = dentalPRRate;
    }

    public String getDentalPRImgUrl() { return dentalPRImgUrl;
    }

    public void setDentalPRImgUrl(String dentalPRImgUrl) { this.dentalPRImgUrl = dentalPRImgUrl;
    }

}
