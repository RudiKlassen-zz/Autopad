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

    public static final String STRINGLIST_SEPERATOR = ", ";
    public static final String YES_CHAR = "Y";

    private HueControl hueControl;


    @Autowired
    public HueShellController(HueControl hueControl) {

        this.hueControl = hueControl;
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

    @ShellMethod("...")
    public String switchSocket(
            @ShellOption(help = "433Mhz radio socket Id [A, B, C, D]") String socketId, @ShellOption(help = "[y,n]") String on) {
        int onoff = getTrueFalse(on) ? 1 : 0;
        try {
            executeShellCommand("./piRCSwitchControl " + socketId + " " + onoff);
            return String.format("Socket %s switched to %s", socketId, on);
        } catch (IOException e) {
            return String.format("The file %s could not be found or read", "piRCSwitchControl");
        } catch (InterruptedException e) {
            return String.format("An error occurred while executing the command");
        }
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

}
