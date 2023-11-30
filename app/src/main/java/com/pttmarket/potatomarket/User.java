package com.pttmarket.potatomarket;

public class User {//게시판에 사용될 리얼타임 db
    private String profile;
    private String id;
    private String product;
    private String price;
    private String email_id;
    private int reportCount;

    public User(){}

    public User(String profile,String product,String price,String id, String email_id){
        this.profile = profile;
        this.product = product;
        this.price = price;
        this.id = id;
        this.email_id = email_id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmailid() {
        return email_id;
    }

    public void setEmailid(String email_Id) {
        this.email_id = email_id;
    }
}