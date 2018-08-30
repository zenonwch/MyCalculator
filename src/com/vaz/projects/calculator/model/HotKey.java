package com.vaz.projects.calculator.model;

public class HotKey {
    private final String key;
    private final String action;

    public HotKey(final String key, final String action) {
        this.key = key;
        this.action = action;
    }

    public String getKey() {
        return key;
    }

    public String getAction() {
        return action;
    }
}
