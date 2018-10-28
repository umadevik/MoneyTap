package com.android.moneytap.pojo;

public class DataModel {

    String name;
    String description;
    int id_;
    String image;


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId_() {
        return id_;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id_=" + id_ +
                ", image='" + image + '\'' +
                '}';
    }
}
