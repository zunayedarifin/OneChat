package com.example.zunay.onechat;

/**
 * Created by zunay on 11/25/2017.
 */

public class Users {
    private String name;
    private String image;
    private String status;
    private String thumbnail;

    public Users()
    {

    }

    public Users(String name, String image, String status,String thumbnail) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumbnail=thumbnail;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_image(){return thumbnail;}

    public void setThumb_image(String thumbnail){this.thumbnail=thumbnail;}


}
