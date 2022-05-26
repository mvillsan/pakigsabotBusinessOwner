package com.capstone.pakigsabotbusinessowner.Restaurant.PromoAndDeals;

public class RestaurantPromoAndDealsModel {
    String restoPADId;
    String restoPADName;
    String restoPADDesc;
    String restoPADStartDate;
    String restoPADEndDate;

    public RestaurantPromoAndDealsModel() {
        //empty constructor needed
    }

    public RestaurantPromoAndDealsModel(String restoPADId, String restoPADName, String restoPADDesc, String restoPADStartDate, String restoPADEndDate) {
        this.restoPADId = restoPADId;
        this.restoPADName = restoPADName;
        this.restoPADDesc = restoPADDesc;
        this.restoPADStartDate = restoPADStartDate;
        this.restoPADEndDate = restoPADEndDate;
    }

    public String getRestoPADId() { return restoPADId;
    }

    public void setRestoPADId(String restoPADId) { this.restoPADId = restoPADId;
    }

    public String getRestoPADName() { return restoPADName;
    }

    public void setRestoPADName(String restoPADName) { this.restoPADName = restoPADName;
    }

    public String getRestoPADDesc() { return restoPADDesc;
    }

    public void setRestoPADDesc(String restoPADDesc) { this.restoPADDesc = restoPADDesc;
    }

    public String getRestoPADStartDate() { return restoPADStartDate;
    }

    public void setRestoPADStartDate(String restoPADStartDate) { this.restoPADStartDate = restoPADStartDate;
    }

    public String getRestoPADEndDate() { return restoPADEndDate;
    }

    public void setRestoPADEndDate(String restoPADEndDate) { this.restoPADEndDate = restoPADEndDate;
    }
}
