package com.rudiklassen.autopad.rest;

/**
 * Request do set the brightness of a Light
 */
public class LightBrightnessRequest {

    private long bri;

    public long getBri() {
        return bri;
    }

    public void setBri(long brightness) {
        this.bri = brightness;
    }

}
