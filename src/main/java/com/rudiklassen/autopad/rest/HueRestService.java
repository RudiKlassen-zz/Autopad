package com.rudiklassen.autopad.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * todo rukl doc und test
 * todo rukl Endpoints in Klasse mit den Uris als Konstanten auslagern
 */
@Service
public class HueRestService {

    private static Logger LOG = LoggerFactory.getLogger(HueRestService.class);

    @Value("${hue.config.usertoken}")
    public String userToken;

    @Value("${hue.config.bridgeip}")
    public String hueIp;

    /**
     * Returns a list of IDs of all lights registered on the Hue Bridge.
     *
     * @throws IOException If the result could not be evaluated
     */
    public List<Long> getAllLightIds() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(getHueBridgeUriWithUserToken() + "/lights", String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> lights = mapper.convertValue(mapper.readTree(resultString), Map.class);

        return stringSetToLongList(lights.keySet());
    }

    /**
     * Switches the given light id on or off
     */
    public void switchLight(long lightId, boolean on) {
        //todo rukl build builder for all requests
        LightSwitchRequest lightSwitchRequest = new LightSwitchRequest();
        lightSwitchRequest.setOn(on);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(getHueBridgeUriWithUserToken() + "/lights/" + lightId + "/state", lightSwitchRequest);
        LOG.info(String.format("Switch light %s to %s", lightId, on));
    }

    String getHueBridgeUriWithUserToken() {
        return "http://" + hueIp + "/api/" + userToken;
    }


    List<Long> stringSetToLongList(Set<String> strings) {
        ArrayList<Long> longs = new ArrayList<>();
        for (String string : strings) {
            longs.add(Long.valueOf(string));
        }
        return longs;
    }

}
