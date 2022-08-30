package com.lucasilva.spaceprobe.service;

import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.model.Probe;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

    public void moveProbe(Probe probe, char[] commands) {
        int x = probe.getPositionX();
        int y = probe.getPositionY();
        int direction = probe.getDirection().getCod();

        for (char command : commands) {
            if(command == 'R') {
                direction = moveProbeToRigth(direction);
            } else if (command == 'L') {
                direction = moveProbeToLeft(direction);
            } else if (command == 'M') {
                switch (direction) {
                    case 0 -> y++;
                    case 1 -> x++;
                    case 2 -> y--;
                    case 3 -> x--;
                }
            }
        }

        probe.setDirection(DirectionProbe.toEnum(direction));
        probe.setPositionX(x);
        probe.setPositionY(y);
    }

    private Integer moveProbeToRigth(int direction) {
        return ++direction % 4;
    }

    private Integer moveProbeToLeft(int direction) {
        return (direction + 3) % 4;
    }
}
