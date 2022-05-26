package com.capstone.pakigsabotbusinessowner.Cafe.Model;

public class CafeMenuItems {
    String cafeFIId;
    String cafeFIName;
    String cafeFICategory;
    String cafeFIDesc;
    String cafeFIAvailability;
    String cafeFIPrice;
    String cafeFIImgUrl;

    public CafeMenuItems() {
        //empty constructor needed
    }

    public CafeMenuItems(String cafeFIId, String cafeFIName, String cafeFICategory, String cafeFIDesc, String cafeFIAvailability, String cafeFIPrice, String cafeFIImgUrl) {
        this.cafeFIId = cafeFIId;
        this.cafeFIName = cafeFIName;
        this.cafeFICategory = cafeFICategory;
        this.cafeFIDesc = cafeFIDesc;
        this.cafeFIAvailability = cafeFIAvailability;
        this.cafeFIPrice = cafeFIPrice;
        this.cafeFIImgUrl = cafeFIImgUrl;
    }

    public String getCafeFIId() { return cafeFIId;
    }

    public void setCafeFIId(String cafeFIId) { this.cafeFIId = cafeFIId;
    }

    public String getCafeFIName() { return cafeFIName;
    }

    public void setCafeFIName(String cafeFIName) { this.cafeFIName = cafeFIName;
    }

    public String getCafeFICategory() { return cafeFICategory;
    }

    public void setCafeFICategory(String cafeFICategory) { this.cafeFICategory = cafeFICategory;
    }

    public String getCafeFIDesc() { return cafeFIDesc;
    }

    public void setCafeFIDesc(String cafeFIDesc) { this.cafeFIDesc = cafeFIDesc;
    }

    public String getCafeFIAvailability() { return cafeFIAvailability;
    }

    public void setCafeFIAvailability(String cafeFIAvailability) { this.cafeFIAvailability = cafeFIAvailability;
    }

    public String getCafeFIPrice() { return cafeFIPrice;
    }

    public void setCafeFIPrice(String cafeFIPrice) { this.cafeFIPrice = cafeFIPrice;
    }

    public String getCafeFIImgUrl() { return cafeFIImgUrl;
    }

    public void setCafeFIImgUrl(String cafeFIImgUrl) { this.cafeFIImgUrl = cafeFIImgUrl;
    }

}
