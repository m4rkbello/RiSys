package com.softwaresolution.water_irrigation.Pojo;

public class UserAccount {
    private String id;
    private String name;
    private String email;
    private String timestamp;
    private String img_url;

    public UserAccount(String id, String name, String email, String timestamp, String img_url) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.timestamp = timestamp;
        this.img_url = img_url;
    }

    public UserAccount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
