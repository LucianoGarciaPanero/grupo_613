package com.example.tp2;

public class RespuestaServicioRegistrar {
    private String state;
    private String env;
    private String token;

    public RespuestaServicioRegistrar(String state, String env, String token) {
        this.state = state;
        this.env = env;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RespuestaServicioRegistrar{" +
                "state='" + state + '\'' +
                ", env='" + env + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

