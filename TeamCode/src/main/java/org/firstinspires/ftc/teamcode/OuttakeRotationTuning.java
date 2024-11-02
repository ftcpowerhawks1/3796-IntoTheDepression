package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import dev.frozenmilk.dairy.core.util.supplier.logical.EnhancedBooleanSupplier;
import dev.frozenmilk.dairy.pasteurized.Pasteurized;
@TeleOp(name="OuttakeRotationTuning", group = "Tuning")
public class OuttakeRotationTuning extends LinearOpMode {

    double Position = -0.1;
    boolean increasing = true;
    boolean decreasing = false;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        Servo LeftServo = hardwareMap.get(Servo.class, "LeftServo");
        Servo RightServo = hardwareMap.get(Servo.class, "RightServo");


        while (opModeIsActive() && !isStopRequested()) {
            if (increasing) {
                Position += 0.1;
            }
            if (decreasing) {
                Position -= 0.1;
            }

            if (Position >=1) {
                Position =1;
                decreasing = true;
                increasing = false;
            }
            if (Position <=0) {
                Position = 0;
                decreasing = false;
                increasing = true;
            }
            LeftServo.setPosition(Position);
            RightServo.setPosition(Position);

            telemetry.addData("Position: ", Position);
            telemetry.addData("decreasing: ", decreasing);
            telemetry.addData("increasing: ", increasing);

            telemetry.update();
            sleep(1000);

        }

    }
}
