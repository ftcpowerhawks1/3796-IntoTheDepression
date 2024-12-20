package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

public class auton extends talonsOpMode {


    @Override
    public void init() {

    }

    @Override
    public void loop() {
        Drive.INSTANCE.timedDriveCommand(50, 0,0.5,0,true,false);

        Drive.INSTANCE.timedDriveCommand(100, 1,0,0,true,false);

        requestOpModeStop();

    }
}
