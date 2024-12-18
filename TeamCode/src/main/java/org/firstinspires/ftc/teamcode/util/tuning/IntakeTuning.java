package org.firstinspires.ftc.teamcode.util.tuning;

import static dev.frozenmilk.dairy.pasteurized.Pasteurized.gamepad1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Config
@TeleOp(name="IntakeTuning", group = "Tuning")
public class IntakeTuning extends OpMode {
    public static double pivot = 0;
    public static double intake = 0;
    public static double rotate = 0;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        telemetry.update();
    }
}