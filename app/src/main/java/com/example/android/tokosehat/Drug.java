package com.example.android.tokosehat;

public class Drug {
    private String name;
    private String diseases;
    private int price;
    private String status;

    public Drug(String name, String diseases, int price, String status) {
        this.name = name;
        this.diseases = diseases;
        this.price = price;
        this.status = status;
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
}
