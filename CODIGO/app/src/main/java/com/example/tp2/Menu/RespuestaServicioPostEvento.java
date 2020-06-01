package com.example.tp2.Menu;

public class RespuestaServicioPostEvento {
    private String state;
    private String env;
    private EventoRecibir event;

    public RespuestaServicioPostEvento(String state, String env, String type_events, String stateE, String description, int group) {
        this.state = state;
        this.env = env;
        this.event = new EventoRecibir(type_events, stateE, description, group);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public EventoRecibir getEvent() {
        return event;
    }

    public void setEvent(EventoRecibir event) {
        this.event = event;
    }
}
