package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;

import dev.frozenmilk.dairy.core.util.supplier.logical.EnhancedBooleanSupplier;
import dev.frozenmilk.dairy.pasteurized.Pasteurized;
import dev.frozenmilk.dairy.pasteurized.PasteurizedGamepad;

public class IntakeRotationTuning extends LinearOpMode {
    EnhancedBooleanSupplier increase = Pasteurized.gamepad1().a();
    EnhancedBooleanSupplier decrease = Pasteurized.gamepad1().b();
    public static final IntakeSubsystem intake = new IntakeSubsystem();
    double position = 0.0;
    @Override
    public void runOpMode() throws InterruptedException {
        if (increase.onTrue()){
            position+=0.1;
        }
        if (decrease.onTrue()){
            position-=0.1;
        }
    }
}
