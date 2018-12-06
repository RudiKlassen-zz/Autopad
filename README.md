## Autopad

### Macropad control for Philips Hue Brdige and 433Mhz radio sockets

Autopad is a small terminal program that runs on a RaspberryPI to control radio sockets and send commands to the Philips Hue Bridge. The commands are sent via string macros from a programmable keyboard to the terminal. The terminal is a Spring Shell application that can be executed on a Raspberry PI. A separate CPP program is used to control the radio sockets. 

## Setup

### Hue Bridge Configuration

To communicate with the Hue Bridge, a new user on the Withelist must first be added to the Bridge (for more information, see https://developers.meethue.com). A token will then be generated, which is required for REST authentication. Both the IP of the bridge in the local network and the token must be adjusted in the _application.properties_. 

Alternatively, these properties can also be set when starting the jar like that.

_java -jar autopad-X.Y.Z.jar --hue.config.bridgeip=192.168.4.2 --hue.config.usertoken=7h1515n07my70k3neb0a5ea7997a99a_

### Radio sockets Control system

Radio sockets: I personally use "Brennenstuhl Radio Switch Set RCS 1000 N Comfort". Transmitter and Receiver: Aukru 433 MHz Radio Transmitter and Receiver Module 

In order to be able to activate and deactivate the individual sockets by radio, it must first be determined which radio signal this is configured for. The receiver and the remote control of the set can be used for this purpose. Tutorials can be found on the net. If the signals are known, they must be adapted in _piRCSwitchControl.cpp_. The program can be recompiled after an adjustment with 

_sudo g++ -DRPI /home/pi/433Utils/rc-switch/RCSwitch.cpp piRCSwitchControl.cpp -o piRCSwitchControl -lwiringPi_

### Konfiguration des Makropads

The macropad can trigger commands by sending corresponding strings to the RaspberryPI or the executing terminal. I used https://github.com/qmk/qmk_firmware to create the keymap and macros on my pad. As macro pad I use the Chocopad of Keeb.io. But it also works with any other programmable keyboard. It is important that the keyboard is connected to the RaspberryPI. 

## Executable commands

The commands implemented up to now can be called after starting the JAR by typing help (or looking at _HueShellController.java_)

*If you have any questions, just shoot me a mail*
