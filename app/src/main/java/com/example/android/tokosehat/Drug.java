package com.example.android.tokosehat;

public class Drug {
    private String name;
    private String diseases;
    private int price;
    private String status;
    private String type;
    private String dosages;
    private String sideEffects;

    public Drug(String name, String diseases, int price, String status, String type, String dosages, String sideEffects) {
        this.name = name;
        this.diseases = diseases;
        this.price = price;
        this.status = status;
        this.type = type;
        this.dosages = dosages;
        this.sideEffects = sideEffects;
    }

    public String getName() {
        return name;
    }

    public String getDiseases() {
        return diseases;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getDosages() {
        return dosages;
    }

    public String getSideEffects() {
        return sideEffects;
    }
}
