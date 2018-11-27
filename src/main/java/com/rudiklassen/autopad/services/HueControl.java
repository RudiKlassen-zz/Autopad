package com.rudiklassen.autopad.services;

import com.rudiklassen.autopad.rest.HueRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Provide a range of functions to control the Philips Hue lamps.
 */
@Service
public class HueControl {

    private static Logger LOG = LoggerFactory.getLogger(HueControl.class);

    private HueRestService hueRestService;

    @Autowired
    public HueControl(HueRestService hueRestService) {

        this.hueRestService = hueRestService;
    }

    public List<Long> getAllLightIds() throws IOException {
        return hueRestService.getAllLightIds();
    }

    public String printInfo() {
        return String.format("User %s Hue Bridge Ip %s", hueRestService.userToken, hueRestService.hueIp);
    }

    /**
     * Checks whether the specified light Id is available. If yes, the corresponding Lamp is switched on or off.
     * Otherwise an {@link IllegalArgumentException} is thrown.
     *
     * @param lightId Id of the Lamp registered on the Hue Bridge
     * @param on      Switch on or off
     */
    public void switchLight(long lightId, boolean on) {

        //check if Light Id is available
        try {
            List<Long> allLightIds = getAllLightIds();
            if (!allLightIds.contains(lightId)) {
                throw new IllegalArgumentException("The given lamps id could not be found");
            }

            hueRestService.switchLight(lightId, on);
        } catch (IOException e) {
            LOG.error("An error has occurred when recalling the registered lights");
        }
    }

    /**
     * Switches all available lamps off or on.
     *
     * @param on Switch on or off
     */
    public void switchAllLights(boolean on) {
        try {
            List<Long> allLightIds = getAllLightIds();

            for (Long id : allLightIds) {
                switchLight(id, on);
            }
        } catch (IOException e) {
            LOG.error("An error has occurred when recalling the registered lights");
        }
    }

    /**
     * Dim the Brightness of a given light Id. This is a scale from the minimum brightness the light is capable of, 1, to the maximum capable brightness, 254.
     */
    public void dimLight(long lightId, long brightness) {
        hueRestService.dimLight(lightId, brightness);
    }

    /**
     * Dim all Lights to specified level.
     */
    public void dimLights(long stepSize) {

        try {
            List<Long> allLightIds = getAllLightIds();

            for (Long id : allLightIds) {
                dimLight(id, stepSize);
            }
        } catch (IOException e) {
            LOG.error("An error has occurred when recalling the registered lights");
        }
    }
}
