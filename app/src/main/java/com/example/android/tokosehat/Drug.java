package com.example.android.tokosehat;

public class Drug {
    private String name;
    private String benefits;
    private String price;
    private String status;
    private String type;
    private String dosage;
    private String sideEffect;

    public Drug(String name, String benefits, String price, String status, String type, String dosage, String sideEffect) {
        this.name = name;
        this.benefits = benefits;
        this.price = price;
        this.status = status;
        this.type = type;
        this.dosage = dosage;
        this.sideEffect = sideEffect;
    }

    public String getName() {
        return name;
    }

    public String getBenefits() {
        return benefits;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getDosage() {
        return dosage;
    }

    public String getSideEffect() {
        return sideEffect;
    }
}
