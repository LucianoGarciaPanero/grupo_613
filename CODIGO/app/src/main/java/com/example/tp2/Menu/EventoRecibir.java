package com.example.tp2.Menu;

public class EventoRecibir {
    private String type_events;
    private String state;
    private String description;
    private int group;

    public EventoRecibir(String type_events, String state, String description, int group) {
        this.type_events = type_events;
        this.state = state;
        this.description = description;
        this.group = group;
    }

    public String getType_events() {
        return type_events;
    }

    public void setType_events(String type_events) {
        this.type_events = type_events;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
