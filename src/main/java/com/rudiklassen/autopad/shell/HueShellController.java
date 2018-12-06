package com.rudiklassen.autopad.shell;

import com.rudiklassen.autopad.services.HueControl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Provides functions to control Hue lamps via the shell
 */
@ShellComponent
public class HueShellController {

    private static Logger LOG = LoggerFactory.getLogger(HueShellController.class);

    public static final int DIM_LIGHTS_MIN_STEPSIZE = 1;
    public static final int DIM_LIGHTS_MAX_STEPSIZE = 5;
    public static final String STRINGLIST_SEPERATOR = ", ";
    public static final String YES_CHAR = "Y";

    private HueControl hueControl;

    @Autowired
    public HueShellController(HueControl hueControl) {
        this.hueControl = hueControl;
    }

    @ShellMethod("Outputs the Ip address of the connected hue and user used")
    public String printBridgeInfo() {
        return hueControl.printInfo();
    }

    @ShellMethod("Return all registered Light Ids of the connected Hue Bridge")
    public String getAllLights() {
        try {
            List<Long> allLightIds = hueControl.getAllLightIds();
            return toPrettyString(allLightIds);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return "An error occurred when evaluating the result";
        }
    }

    @ShellMethod("Switch the given Hue Light Id on or off")
    public void switchLightById(
            @ShellOption(help = "[y,n]") String on,
            @ShellOption(help = "Light Id provided by the Hue Bridge") long lightId) {
        hueControl.switchLight(lightId, getTrueFalse(on));
    }

    @ShellMethod("Switches all available lamps off or on.")
    public void switchAllLamps(
            @ShellOption(help = "[y,n]") String on) {
        hueControl.switchAllLights(getTrueFalse(on));
    }

    @ShellMethod("Switches a radio socket on or off")
    public String switchSocket(
            @ShellOption(help = "433Mhz radio socket Id [1, 2, 3, 4]") String socketId, //
            @ShellOption(help = "[y,n]") String on//
    ) throws IOException, InterruptedException {
        int onoff = getTrueFalse(on) ? 1 : 0;
        long socket = Long.valueOf(socketId);

        executeShellCommand("./piRCSwitchControl " + socket + " " + onoff);
        return String.format("Socket %s switched to %s", socketId, on);
    }

    @ShellMethod("Dim all Lights to a specified level")
    public String dimLights(@ShellOption(help = "Step[1 = 20%, 2 = 40, 3 = 60%, 4 = 80%, 5 = 100%]") String step) {
        int stepSize = Integer.valueOf(step);
        if (stepSize > 5 || stepSize < 1) {
            return String.format("Stepsize was not accepted. Choose a number between %s and %s", DIM_LIGHTS_MIN_STEPSIZE, DIM_LIGHTS_MAX_STEPSIZE);
        }

        hueControl.dimLights(stepSizeToScalar(stepSize));
        return String.format("Set Dimlevel to %s", stepSize);
    }


    void executeShellCommand(String command) throws IOException, InterruptedException {
        Process process;
        process = Runtime.getRuntime().exec(String.format(command));

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

    boolean getTrueFalse(String yesNo) {
        String s = yesNo.toUpperCase().trim();
        return (s.equals(YES_CHAR));
    }

    /**
     * Generates a comma separated {@link String} from a list of {@link Long}.
     */
    String toPrettyString(List<Long> strings) {
        String result = StringUtils.EMPTY;
        boolean first = true;
        for (Long string : strings) {
            if (!first) {
                result += STRINGLIST_SEPERATOR;
            }
            result += string;
            first = false;
        }

        return result;
    }

    /**
     * Translate the Stepsie to a Scalar from 1 to 254 and returns thes result.
     */
    private int stepSizeToScalar(int stepSize) {
        switch (stepSize) {
            case 1:
                return 50;
            case 2:
                return 100;
            case 3:
                return 150;
            case 4:
                return 200;

            default:
                return 254;
        }
    }

}
