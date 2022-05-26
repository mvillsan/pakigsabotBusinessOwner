package com.example.pakigsabot.Clinics.DentalClinic.Models;

public class DentalClinicModel {

    String est_id;
    String est_Name;
    String est_address;
    String est_image;
    String est_phoneNum;

    public DentalClinicModel() {
        //empty constructor needed
    }

    //constructor
    public DentalClinicModel(String est_id, String est_Name, String est_address, String est_image, String est_phoneNum) {
        this.est_id = est_id;
        this.est_Name = est_Name;
        this.est_address = est_address;
        this.est_image = est_image;
        this.est_phoneNum = est_phoneNum;
    }


    public String getEst_id() { return est_id;
    }

    public void setEst_id(String est_id) { this.est_id = est_id;
    }

    public String getEst_Name() { return est_Name;
    }

    public void setEst_Name(String est_Name) { this.est_Name = est_Name;
    }

    public String getEst_address() { return est_address;
    }

    public void setEst_address(String est_address) { this.est_address = est_address;
    }

    public String getEst_image() { return est_image;
    }

    public void setEst_image(String est_image) { this.est_image = est_image;
    }

    public String getEst_phoneNum() { return est_phoneNum;
    }

    public void setEst_phoneNum(String est_phoneNum) { this.est_phoneNum = est_phoneNum;
    }


}
