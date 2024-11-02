package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;

import dev.frozenmilk.dairy.core.util.supplier.logical.EnhancedBooleanSupplier;
import dev.frozenmilk.dairy.pasteurized.Pasteurized;


@HorizontalSlides.Attach
@TeleOp(name="HorizontalPIDTuning", group = "Tuning")
public class HorizontalPIDTuning extends LinearOpMode {
    EnhancedBooleanSupplier increase = Pasteurized.gamepad1().a();
    EnhancedBooleanSupplier decrease = Pasteurized.gamepad1().b();
    public static final HorizontalSlides HORIZONTAL_SLIDES = new HorizontalSlides();
    double position = 500;

    @Override
    public void runOpMode() throws InterruptedException {

        while (opModeIsActive() && isStopRequested()) {
            if (increase.onTrue()) {
                position += 5;
            }
            if (decrease.onTrue()) {
                position -= 5;
            }

            HORIZONTAL_SLIDES.runToPosition(position);
            telemetry.addData("Position: ", position);
            telemetry.update();

        }

    }
}
