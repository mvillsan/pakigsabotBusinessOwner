package com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic;

public class DentalClinicPromoAndDealsModel {
    String dentalPADId;
    String dentalPADName;
    String dentalPADDesc;
    String dentalPADStartDate;
    String dentalPADEndDate;

    public DentalClinicPromoAndDealsModel() {
        //empty constructor needed
    }


    public DentalClinicPromoAndDealsModel(String dentalPADId, String dentalPADName, String dentalPADDesc, String dentalPADStartDate, String dentalPADEndDate) {
        this.dentalPADId = dentalPADId;
        this.dentalPADName = dentalPADName;
        this.dentalPADDesc = dentalPADDesc;
        this.dentalPADStartDate = dentalPADStartDate;
        this.dentalPADEndDate = dentalPADEndDate;
    }

    public String getDentalPADId() { return dentalPADId;
    }

    public void setDentalPADId(String dentalPADId) { this.dentalPADId = dentalPADId;
    }

    public String getDentalPADName() { return dentalPADName;
    }

    public void setDentalPADName(String dentalPADName) { this.dentalPADName = dentalPADName;
    }

    public String getDentalPADDesc() { return dentalPADDesc;
    }

    public void setDentalPADDesc(String dentalPADDesc) { this.dentalPADDesc = dentalPADDesc;
    }

    public String getDentalPADStartDate() { return dentalPADStartDate;
    }

    public void setDentalPADStartDate(String dentalPADStartDate) { this.dentalPADStartDate = dentalPADStartDate;
    }

    public String getDentalPADEndDate() { return dentalPADEndDate;
    }

    public void setDentalPADEndDate(String dentalPADEndDate) { this.dentalPADEndDate = dentalPADEndDate;
    }
    

}
