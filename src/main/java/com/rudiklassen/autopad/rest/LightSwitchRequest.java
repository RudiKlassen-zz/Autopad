package com.rudiklassen.autopad.rest;

/**
 * Request to switch a light on and off
 */
public class LightSwitchRequest {

    private boolean on;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
