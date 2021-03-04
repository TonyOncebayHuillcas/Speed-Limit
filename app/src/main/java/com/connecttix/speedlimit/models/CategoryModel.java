package com.connecttix.speedlimit.models;

public class CategoryModel {
    private int id;
    private int id_category;
    private String name;
    private String description;

    public CategoryModel(){}

    public CategoryModel(int id_category, String name, String description) {
        this.id_category = id_category;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
