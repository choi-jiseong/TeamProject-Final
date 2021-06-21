package com.example.teamproject;

public class ProfessorStatusModel {
    String id;
    String name;
    String status;

    public ProfessorStatusModel(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return  status;
    }
}

