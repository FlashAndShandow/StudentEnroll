package com.guo.entity;

public class Student {
    Integer id;
    String name;
    String identityNumber;
    String portraitPath;

    public Student(Integer id, String name, String identityNumber, String portraitPath) {
        this.id = id;
        this.name = name;
        this.identityNumber = identityNumber;
        this.portraitPath = portraitPath;
    }

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getIdentityNumber() { return identityNumber; }
    public String getPortraitPath() { return portraitPath; }
}