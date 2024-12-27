package org.firstinspires.ftc.teamcode.opmodes.testingOpmodes;

import static dev.frozenmilk.dairy.pasteurized.Pasteurized.gamepad1;
import static dev.frozenmilk.mercurial.Mercurial.gamepad2;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Drive.Attach
@HorizontalSlides.Attach
@Intake.Attach
@Config
@Disabled
@TeleOp(name="cycling", group = "Tuning")
public class cycling extends talonsOpMode {
    public static double target = 0;
    ColorSensor color;

    @Override
    public void init() {
        //just to make slides tuned in a similar environment to when everything is running
        //ignore everything in init
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));

        color = hardwareMap.get(ColorSensor.class, "color");
    }

    @Override
    public void loop() {

        if (color.red() >= 100 || color.blue() >= 100) {
            target = 0;
        }

        HorizontalSlides.INSTANCE.setTarget(target);
        telemetry.addData("Target: ", target);
        telemetry.addData("Position: ", HorizontalSlides.INSTANCE.getEncoder());
        telemetry.update();
    }
}