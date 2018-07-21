package com.example.android.tokosehat;

import com.example.android.tokosehat.data.DrugContract.DrugEntry;

public class Drug {
    private String name;
    private String benefits;
    private int itemPrice;
    private int dozenPrice;
    private int boxPrice;
    private String status;
    private String type;
    private String dosage;
    private String sideEffect;

    public Drug(String name, int itemPrice, int dozenPrice, int boxPrice, String type) {
        this.name = name;
        this.benefits = "-";
        this.itemPrice = itemPrice;
        this.dozenPrice = dozenPrice;
        this.boxPrice = boxPrice;
        this.status = DrugEntry.STATUS_AVAILABLE;
        this.type = type;
        this.dosage = "-";
        this.sideEffect = "-";
    }

    public Drug(String name, String benefits, int itemPrice, int dozenPrice, int boxPrice, String status, String type, String dosage, String sideEffect) {
        this.name = name;
        this.benefits = benefits;
        this.itemPrice = itemPrice;
        this.dozenPrice = dozenPrice;
        this.boxPrice = boxPrice;
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

    public int getItemPrice() {
        return itemPrice;
    }

    public int getDozenPrice() {
        return dozenPrice;
    }

    public int getBoxPrice() {
        return boxPrice;
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
