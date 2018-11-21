package com.example.user.demo.models;

/**
 * Created by User on 01.03.2016.
 */
public class BluetoothModel {


    private String address;
    private Integer type;
    private String name;


    public BluetoothModel(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public BluetoothModel() {
    }

    public String getAddress() {
        return address != null ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }
}
