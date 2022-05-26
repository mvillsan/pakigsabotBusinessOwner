package com.example.pakigsabot.Favorites;

public class FavoritesModel {
    String favEstId;
    String estName;
    String estAddress;
    String estImageUrl;

    public FavoritesModel() {
        //empty constructor needed
    }


    public FavoritesModel(String favEstId, String estName, String estAddress, String estImageUrl) {
        this.favEstId = favEstId;
        this.estName = estName;
        this.estAddress = estAddress;
        this.estImageUrl = estImageUrl;
    }

    public String getFavEstId() {
        return favEstId;
    }

    public void setFavEstId(String favEstId) {
        this.favEstId = favEstId;
    }

    public String getEstName() {
        return estName;
    }

    public void setEstName(String estName) {
        this.estName = estName;
    }

    public String getEstAddress() {
        return estAddress;
    }

    public void setEstAddress(String estAddress) {
        this.estAddress = estAddress;
    }

    public String getEstImageUrl() {
        return estImageUrl;
    }

    public void setEstImageUrl(String estImageUrl) {
        this.estImageUrl = estImageUrl;
    }





}
