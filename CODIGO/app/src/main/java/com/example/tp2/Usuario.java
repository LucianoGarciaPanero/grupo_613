package com.example.tp2;

public class Usuario {
    private String name;
    private String lastname;
    private int dni;
    private String email;
    private int comission;
    private int group;

    public Usuario(String name, String lastname, int dni, String email, int comission, int group) {
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.email = email;
        this.comission = comission;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getComission() {
        return comission;
    }

    public void setComission(int comission) {
        this.comission = comission;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
