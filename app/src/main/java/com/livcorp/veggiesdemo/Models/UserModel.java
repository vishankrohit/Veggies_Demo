package com.livcorp.veggiesdemo.Models;

public class UserModel {
    private String Name;
    private String Address;
    private String Area_City;
    private String State;
    private String PinCode;
    private String Phone;

    public UserModel(String name, String address, String area_City, String state, String pinCode, String phone) {
        Name = name;
        Address = address;
        Area_City = area_City;
        State = state;
        PinCode = pinCode;
        Phone = phone;
    }

    public UserModel(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getArea_City() {
        return Area_City;
    }

    public void setArea_City(String area_City) {
        Area_City = area_City;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

}
