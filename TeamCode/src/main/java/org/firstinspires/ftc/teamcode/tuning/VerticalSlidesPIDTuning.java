package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Drive.Attach
@VerticalSlides.Attach
@Config
@TeleOp(name="VerticalSlidesPIDTuning", group = "Tuning")
public class VerticalSlidesPIDTuning extends OpMode {
    public static double target = 0;

    @Override
    public void init() {
        //just to make slides tuned in a similar environment to when everything is running
        //ignore everything in init
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true,false));
    }

    @Override
    public void loop() {
        VerticalSlides.INSTANCE.setTarget(target);
        telemetry.addData("Target: ", target);
        telemetry.addData("Position: ", VerticalSlides.INSTANCE.getEncoder());
        telemetry.update();
    }
}