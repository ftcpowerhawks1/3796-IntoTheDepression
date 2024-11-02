package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;

import dev.frozenmilk.dairy.core.util.supplier.logical.EnhancedBooleanSupplier;
import dev.frozenmilk.dairy.pasteurized.Pasteurized;
import dev.frozenmilk.dairy.pasteurized.PasteurizedGamepad;

@TeleOp(name="IntakeRotationTuning", group = "Tuning")
public class IntakeRotationTuning extends LinearOpMode {
    EnhancedBooleanSupplier increase = Pasteurized.gamepad1().a();
    EnhancedBooleanSupplier decrease = Pasteurized.gamepad1().b();
    public static final IntakeSubsystem intake = new IntakeSubsystem();
    double position = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {

        while (opModeIsActive() && isStopRequested()) {
            if (increase.onTrue()) {
                position += 0.1;
            }
            if (decrease.onTrue()) {
                position -= 0.1;
            }

            if (position != intake.getPosition()) {
                intake.setPosition(position);
            }
            telemetry.addData("Position: ", position);
            telemetry.update();

        }

    }
}
