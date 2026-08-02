package com.example.ecosort;


import java.io.Serializable;

public class Waste implements Serializable {

    int id;
    String name;
    String tips;
    String image;
    String binColor;
    String category;
    boolean recyclable;
    String description;
    String disposalMethod;
    String decompositionTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBinColor() {
        return binColor;
    }

    public void setBinColor(String binColor) {
        this.binColor = binColor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRecyclable() {
        return recyclable;
    }

    public void setRecyclable(boolean recyclable) {
        this.recyclable = recyclable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisposalMethod() {
        return disposalMethod;
    }

    public void setDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public String getDecompositionTime() {
        return decompositionTime;
    }

    public void setDecompositionTime(String decompositionTime) {
        this.decompositionTime = decompositionTime;
    }
}
