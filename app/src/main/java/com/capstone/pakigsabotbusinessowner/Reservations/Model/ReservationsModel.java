package com.capstone.pakigsabotbusinessowner.Reservations.Model;

public class ReservationsModel {
    String reserveAutoId;
    String rStatus_default;
    String reservePax;
    String reserveName;
    String reserveDateIn;
    String reserveDateOut;
    String reserveTime;
    String cust_ID;
    String custFName;
    String custLName;
    String cust_MobileNum;
    String cust_EmailAddress;
    String estID;
    String estName;
    String est_EmailAddress;
    String fee;
    String notes;
    String adultPax;
    String childPax;
    String infantPax;
    String petPax;
    String dateOfTransaction;
    String confirmedDate;


    public ReservationsModel() {
        //empty constructor needed
    }


    public ReservationsModel(String reserveAutoId, String rStatus_default, String reservePax, String reserveName, String reserveDateIn, String reserveDateOut, String reserveTime,
                             String cust_ID, String custFName, String custLName, String cust_MobileNum, String cust_EmailAddress, String estID, String estName, String est_EmailAddress,
                             String fee, String notes, String adultPax, String childPax, String infantPax, String petPax, String dateOfTransaction, String confirmedDate) {
        this.reserveAutoId = reserveAutoId;
        this.rStatus_default = rStatus_default;
        this.reservePax = reservePax;
        this.reserveName = reserveName;
        this.reserveDateIn = reserveDateIn;
        this.reserveDateOut = reserveDateOut;
        this.reserveTime = reserveTime;
        this.cust_ID = cust_ID;
        this.custFName = custFName;
        this.custLName = custLName;
        this.cust_MobileNum = cust_MobileNum;
        this.cust_EmailAddress = cust_EmailAddress;
        this.estID = estID;
        this.estName = estName;
        this.est_EmailAddress = est_EmailAddress;
        this.fee = fee;
        this.notes = notes;
        this.adultPax = adultPax;
        this.childPax = childPax;
        this.infantPax = infantPax;
        this.petPax = petPax;
        this.dateOfTransaction = dateOfTransaction;
        this.confirmedDate = confirmedDate;
    }

    public String getReserveAutoId() {
        return reserveAutoId;
    }

    public void setReserveAutoId(String reserveAutoId) {
        this.reserveAutoId = reserveAutoId;
    }

    public String getrStatus_default() {
        return rStatus_default;
    }

    public void setrStatus_default(String rStatus_default) {
        this.rStatus_default = rStatus_default;
    }

    public String getReservePax() {
        return reservePax;
    }

    public void setReservePax(String reservePax) {
        this.reservePax = reservePax;
    }

    public String getReserveName() {
        return reserveName;
    }

    public void setReserveName(String reserveName) {
        this.reserveName = reserveName;
    }

    public String getReserveDateIn() {
        return reserveDateIn;
    }

    public void setReserveDateIn(String reserveDateIn) {
        this.reserveDateIn = reserveDateIn;
    }

    public String getReserveDateOut() {
        return reserveDateOut;
    }

    public void setReserveDateOut(String reserveDateOut) {
        this.reserveDateOut = reserveDateOut;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getCust_ID() {
        return cust_ID;
    }

    public void setCust_ID(String cust_ID) {
        this.cust_ID = cust_ID;
    }

    public String getCustFName() {
        return custFName;
    }

    public void setCustFName(String custFName) {
        this.custFName = custFName;
    }

    public String getCustLName() {
        return custLName;
    }

    public void setCustLName(String custLName) {
        this.custLName = custLName;
    }

    public String getCust_MobileNum() {
        return cust_MobileNum;
    }

    public void setCust_MobileNum(String cust_MobileNum) {
        this.cust_MobileNum = cust_MobileNum;
    }

    public String getCust_EmailAddress() {
        return cust_EmailAddress;
    }

    public void setCust_EmailAddress(String cust_EmailAddress) {
        this.cust_EmailAddress = cust_EmailAddress;
    }

    public String getEstID() {
        return estID;
    }

    public void setEstID(String estID) {
        this.estID = estID;
    }

    public String getEstName() {
        return estName;
    }

    public void setEstName(String estName) {
        this.estName = estName;
    }

    public String getEst_EmailAddress() {
        return est_EmailAddress;
    }

    public void setEst_EmailAddress(String est_EmailAddress) {
        this.est_EmailAddress = est_EmailAddress;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAdultPax() {
        return adultPax;
    }

    public void setAdultPax(String adultPax) {
        this.adultPax = adultPax;
    }

    public String getChildPax() {
        return childPax;
    }

    public void setChildPax(String childPax) {
        this.childPax = childPax;
    }

    public String getInfantPax() {
        return infantPax;
    }

    public void setInfantPax(String infantPax) {
        this.infantPax = infantPax;
    }

    public String getPetPax() {
        return petPax;
    }

    public void setPetPax(String petPax) {
        this.petPax = petPax;
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }


}
