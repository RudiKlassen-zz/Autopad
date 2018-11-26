package com.rudiklassen.autopad.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HueRestServiceTest {

    private String jsonstring = "{\n" +
            "\t\"1\": {\n" +
            "\t\t\"state\": {\n" +
            "\t\t\t\"on\": true,\n" +
            "\t\t\t\"bri\": 254,\n" +
            "\t\t\t\"hue\": 15701,\n" +
            "\t\t\t\"sat\": 20,\n" +
            "\t\t\t\"effect\": \"none\",\n" +
            "\t\t\t\"xy\": [\n" +
            "\t\t\t\t0.3797,\n" +
            "\t\t\t\t0.3903\n" +
            "\t\t\t],\n" +
            "\t\t\t\"ct\": 247,\n" +
            "\t\t\t\"alert\": \"none\",\n" +
            "\t\t\t\"colormode\": \"xy\",\n" +
            "\t\t\t\"mode\": \"homeautomation\",\n" +
            "\t\t\t\"reachable\": true\n" +
            "\t\t},\n" +
            "\t\t\"swupdate\": {\n" +
            "\t\t\t\"state\": \"noupdates\",\n" +
            "\t\t\t\"lastinstall\": \"2017-11-15T13:22:56\"\n" +
            "\t\t},\n" +
            "\t\t\"type\": \"Extended color light\",\n" +
            "\t\t\"name\": \"Hue go 1\",\n" +
            "\t\t\"modelid\": \"LLC020\",\n" +
            "\t\t\"manufacturername\": \"Philips\",\n" +
            "\t\t\"productname\": \"Hue go\",\n" +
            "\t\t\"capabilities\": {\n" +
            "\t\t\t\"certified\": true,\n" +
            "\t\t\t\"control\": {\n" +
            "\t\t\t\t\"mindimlevel\": 40,\n" +
            "\t\t\t\t\"maxlumen\": 300,\n" +
            "\t\t\t\t\"colorgamuttype\": \"C\",\n" +
            "\t\t\t\t\"colorgamut\": [\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.6915,\n" +
            "\t\t\t\t\t\t0.3083\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.17,\n" +
            "\t\t\t\t\t\t0.7\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.1532,\n" +
            "\t\t\t\t\t\t0.0475\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"ct\": {\n" +
            "\t\t\t\t\t\"min\": 153,\n" +
            "\t\t\t\t\t\"max\": 500\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"streaming\": {\n" +
            "\t\t\t\t\"renderer\": true,\n" +
            "\t\t\t\t\"proxy\": true\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"config\": {\n" +
            "\t\t\t\"archetype\": \"huego\",\n" +
            "\t\t\t\"function\": \"decorative\",\n" +
            "\t\t\t\"direction\": \"omnidirectional\"\n" +
            "\t\t},\n" +
            "\t\t\"uniqueid\": \"00:17:88:01:01:18:a0:db-0b\",\n" +
            "\t\t\"swversion\": \"5.105.0.21169\"\n" +
            "\t},\n" +
            "\t\"2\": {\n" +
            "\t\t\"state\": {\n" +
            "\t\t\t\"on\": true,\n" +
            "\t\t\t\"bri\": 38,\n" +
            "\t\t\t\"hue\": 40257,\n" +
            "\t\t\t\"sat\": 127,\n" +
            "\t\t\t\"effect\": \"none\",\n" +
            "\t\t\t\"xy\": [\n" +
            "\t\t\t\t0.2929,\n" +
            "\t\t\t\t0.3109\n" +
            "\t\t\t],\n" +
            "\t\t\t\"alert\": \"none\",\n" +
            "\t\t\t\"colormode\": \"xy\",\n" +
            "\t\t\t\"mode\": \"homeautomation\",\n" +
            "\t\t\t\"reachable\": true\n" +
            "\t\t},\n" +
            "\t\t\"swupdate\": {\n" +
            "\t\t\t\"state\": \"noupdates\",\n" +
            "\t\t\t\"lastinstall\": \"2017-11-15T13:22:47\"\n" +
            "\t\t},\n" +
            "\t\t\"type\": \"Color light\",\n" +
            "\t\t\"name\": \"Hue bloom 1\",\n" +
            "\t\t\"modelid\": \"LLC011\",\n" +
            "\t\t\"manufacturername\": \"Philips\",\n" +
            "\t\t\"productname\": \"Hue bloom\",\n" +
            "\t\t\"capabilities\": {\n" +
            "\t\t\t\"certified\": true,\n" +
            "\t\t\t\"control\": {\n" +
            "\t\t\t\t\"mindimlevel\": 10000,\n" +
            "\t\t\t\t\"maxlumen\": 120,\n" +
            "\t\t\t\t\"colorgamuttype\": \"A\",\n" +
            "\t\t\t\t\"colorgamut\": [\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.704,\n" +
            "\t\t\t\t\t\t0.296\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.2151,\n" +
            "\t\t\t\t\t\t0.7106\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.138,\n" +
            "\t\t\t\t\t\t0.08\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t\"streaming\": {\n" +
            "\t\t\t\t\"renderer\": true,\n" +
            "\t\t\t\t\"proxy\": false\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"config\": {\n" +
            "\t\t\t\"archetype\": \"huebloom\",\n" +
            "\t\t\t\"function\": \"decorative\",\n" +
            "\t\t\t\"direction\": \"upwards\"\n" +
            "\t\t},\n" +
            "\t\t\"uniqueid\": \"00:17:88:01:00:1e:20:79-0b\",\n" +
            "\t\t\"swversion\": \"5.105.1.21778\"\n" +
            "\t},\n" +
            "\t\"3\": {\n" +
            "\t\t\"state\": {\n" +
            "\t\t\t\"on\": true,\n" +
            "\t\t\t\"bri\": 254,\n" +
            "\t\t\t\"hue\": 15701,\n" +
            "\t\t\t\"sat\": 20,\n" +
            "\t\t\t\"effect\": \"none\",\n" +
            "\t\t\t\"xy\": [\n" +
            "\t\t\t\t0.3797,\n" +
            "\t\t\t\t0.3903\n" +
            "\t\t\t],\n" +
            "\t\t\t\"ct\": 247,\n" +
            "\t\t\t\"alert\": \"none\",\n" +
            "\t\t\t\"colormode\": \"xy\",\n" +
            "\t\t\t\"mode\": \"homeautomation\",\n" +
            "\t\t\t\"reachable\": true\n" +
            "\t\t},\n" +
            "\t\t\"swupdate\": {\n" +
            "\t\t\t\"state\": \"noupdates\",\n" +
            "\t\t\t\"lastinstall\": \"2018-08-28T18:30:40\"\n" +
            "\t\t},\n" +
            "\t\t\"type\": \"Extended color light\",\n" +
            "\t\t\"name\": \"Hue go 2\",\n" +
            "\t\t\"modelid\": \"LLC020\",\n" +
            "\t\t\"manufacturername\": \"Philips\",\n" +
            "\t\t\"productname\": \"Hue go\",\n" +
            "\t\t\"capabilities\": {\n" +
            "\t\t\t\"certified\": true,\n" +
            "\t\t\t\"control\": {\n" +
            "\t\t\t\t\"mindimlevel\": 40,\n" +
            "\t\t\t\t\"maxlumen\": 300,\n" +
            "\t\t\t\t\"colorgamuttype\": \"C\",\n" +
            "\t\t\t\t\"colorgamut\": [\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.6915,\n" +
            "\t\t\t\t\t\t0.3083\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.17,\n" +
            "\t\t\t\t\t\t0.7\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t[\n" +
            "\t\t\t\t\t\t0.1532,\n" +
            "\t\t\t\t\t\t0.0475\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"ct\": {\n" +
            "\t\t\t\t\t\"min\": 153,\n" +
            "\t\t\t\t\t\"max\": 500\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"streaming\": {\n" +
            "\t\t\t\t\"renderer\": true,\n" +
            "\t\t\t\t\"proxy\": true\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"config\": {\n" +
            "\t\t\t\"archetype\": \"huego\",\n" +
            "\t\t\t\"function\": \"decorative\",\n" +
            "\t\t\t\"direction\": \"omnidirectional\"\n" +
            "\t\t},\n" +
            "\t\t\"uniqueid\": \"00:17:88:01:04:7a:39:5c-0b\",\n" +
            "\t\t\"swversion\": \"5.105.0.21169\"\n" +
            "\t}\n" +
            "}".trim();

    @Test
    public void sdf() {

    }
}