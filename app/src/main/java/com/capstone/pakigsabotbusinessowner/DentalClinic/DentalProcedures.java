package com.capstone.pakigsabotbusinessowner.DentalClinic;

public class DentalProcedures {
    String procName,  procDesc;
    Double procRate;

    //required empty constructor
    public DentalProcedures() {
    }

    //constructor
    public DentalProcedures(String procName, String procDesc, Double procRate) {

        this.procName = procName;
        this.procDesc = procDesc;
        this.procRate = procRate;


    }

    //required value fetching the data from the firestore
    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcDesc() {
        return procDesc;
    }

    public void setProcDesc(String procDesc) {
        this.procDesc = procDesc;
    }

    public Double getProcRate() {
        return procRate;
    }

    public void setProcRate(Double procRate) {
        this.procRate = procRate;
    }
}
