#include "/home/pi/433Utils/rc-switch/RCSwitch.h"
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[]) {

    int pin = 0; //wPi
    int codeSocketADon = 5506385;
    int codeSocketADoff = 5506388;

    int codeSocketBDon = 5509457;
    int codeSocketBDoff = 5509460;

    int codeSocketCDon = 5510225;
    int codeSocketCDoff = 5510228;

    int codeSocketDDon = 5510417;
    int codeSocketDDoff = 5510420;

    if (wiringPiSetup() == -1) return 1;

    RCSwitch piSwitch = RCSwitch();
    piSwitch.enableTransmit(pin);

	int currentSocketOn;
	int currentSocketOff;
	int socket = atoi(argv[1]);

	//Find requestet Socket
	if(socket == 1){
		currentSocketOn = codeSocketADon;
		currentSocketOff = codeSocketADoff;
	} else if(socket == 2){
		currentSocketOn = codeSocketBDon;
		currentSocketOff = codeSocketBDoff;
	} else if(socket == 3){
		currentSocketOn = codeSocketCDon;
		currentSocketOff = codeSocketCDoff;
	} else if(socket== 4){
		currentSocketOn = codeSocketDDon;
		currentSocketOff = codeSocketDDoff;
	} else {
	  printf("Socket is not available");
	}

	//Switch Socket on or off
    if (atoi(argv[2]) == 1) {
        piSwitch.send(currentSocketOn, 24);
        printf("Turn on Port %i - Signal %i \n\r", socket, currentSocketOn);
    } else {
        piSwitch.send(currentSocketOff, 24);
        printf("Turn on Port %i - Signal %i \n\r", socket, currentSocketOff);
    }
    return 0;
}

