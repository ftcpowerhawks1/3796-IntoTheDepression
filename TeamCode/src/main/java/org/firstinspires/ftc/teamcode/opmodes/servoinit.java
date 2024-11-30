package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Config
@TeleOp(name="servoinit", group = "Tuning")
public class servoinit extends OpMode {
    public static double target = 0;
    Servo leftSide, rightSide;
    ColorSensor color;
    @Override
    public void init() {

        leftSide = hardwareMap.get(Servo.class, "LeftOuttakeServo");
        rightSide = hardwareMap.get(Servo.class, "RightOuttakeServo");

    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            leftSide.setPosition(0);
            rightSide.setPosition(0);
        } else if (gamepad1.b){
            leftSide.setPosition(0.6);
            rightSide.setPosition(0.6);
        }
    }
}