package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach
@HorizontalSlides.Attach
@VerticalSlides.Attach
@IntakeSubsystem.Attach
@TeleOp(name = "BlueTeleop", group = "Teleop")
public class REDTeleop extends RobotHardware {

    @Override
    public void init() {
    }

    @Override
    public void loop() {

    }
}
