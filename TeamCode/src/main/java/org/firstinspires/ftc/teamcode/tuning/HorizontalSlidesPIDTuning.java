package org.firstinspires.ftc.teamcode.tuning;

import static dev.frozenmilk.dairy.pasteurized.Pasteurized.gamepad1;
import static dev.frozenmilk.mercurial.Mercurial.gamepad2;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Drive.Attach
@HorizontalSlides.Attach
@Config
@TeleOp(name="HorizontalSlidesPIDTuning", group = "Tuning")
public class HorizontalSlidesPIDTuning extends OpMode {
    public static double target = 0;

    @Override
    public void init() {
        //just to make slides tuned in a similar environment to when everything is running
        //ignore everything in init
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
    }

    @Override
    public void loop() {
        HorizontalSlides.INSTANCE.setTarget(target);
        telemetry.addData("Target: ", target);
        telemetry.addData("Position: ", HorizontalSlides.INSTANCE.getEncoder());
        telemetry.update();
    }
}