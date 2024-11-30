/*
package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Intake.*;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class Intake extends SDKSubsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    public enum IntakePivotState {
        INTAKE,
        HOME
    }

    public static IntakePivotState intakePivotState;

    //hardware
    private final Cell<CachingServo> intakePivot = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intakePivotLeft)));
    private final Cell<CachingServo> intake = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(Servo.class, Constants.Intake.intake)));

    //set target method
    public void setPivotPosition(double position) {
        intakePivotLeft.get().setPosition(position);
        intakePivotRight.get().setPosition(position);
    }
    public double getPivotPosition() {
        return intakePivotLeft.get().getPosition();
    }
    public void setPivot(IntakePivotState intakePivotState) {
        switch (intakePivotState) {
            case SCORING:
                intakePivotLeft.get().setPosition(scoringPos);
                intakePivotRight.get().setPosition(scoringPos);
                break;
            case SPECIMEN_SCORING:
                intakePivotLeft.get().setPosition(specimenScoringPos);
                intakePivotRight.get().setPosition(specimenScoringPos);
                break;
            case INTAKE:
                intakePivotLeft.get().setPosition(intakePos);
                intakePivotRight.get().setPosition(intakePos);
                break;
            case HOME:
                intakePivotLeft.get().setPosition(homePos);
                intakePivotRight.get().setPosition(homePos);
                break;
        }
        Intake.intakePivotState = intakePivotState;
    }

    public void setClawPosition(double position) {
        intake.get().setPosition(position);
    }

    public void setRotation(double rotation) {
        rotatingIntake.get().setPosition(rotation);
    }

    public void clawOpen(boolean open) {

        if (open) {
            intake.get().setPosition(clawOpenPos);
            Intake.clawState = ClawState.OPEN;
        }
        else {
            intake.get().setPosition(clawClosedPos);
            Intake.clawState = ClawState.CLOSED;
        }
    }

    public void clawOpenAndClose() {
        switch (Intake.clawState) {
            case OPEN:
                intake.get().setPosition(clawClosedPos);
                Intake.clawState = ClawState.CLOSED;
                break;
            case CLOSED:
                intake.get().setPosition(clawOpenPos);
                Intake.clawState = ClawState.OPEN;
                break;
        }
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        intakePivotRight.get().setDirection(Servo.Direction.REVERSE);
        intake.get().setDirection(Servo.Direction.REVERSE);
    }

    public Lambda setIntakePivot(IntakePivotState intakePivotState) {
        return new Lambda("setIntakePivot")
                .setInit(() -> setPivot(intakePivotState));
    }

    public Lambda setIntakePivotDependent() {
        Intake.IntakePivotState intakePos;
        if (Arm.armState == HOME) {
            intakePos = IntakePivotState.HOME;
        }
        else if (Arm.armState == HIGH_SCORING) {
            intakePos = IntakePivotState.INTAKE;
        }
        else {
            intakePos = IntakePivotState.HOME;
        }
        return new Lambda("setIntakePivotDependent")
                .setInit(() -> setIntakePivot(intakePos));
    }

    public Lambda setClawOpen(boolean open) {
        return new Lambda("setClaw")
                .setInit(() -> clawOpen(open));
    }

    public Lambda setClawOpenAndClose() {
        return new Lambda("setClawOpenAndClose")
                .setInit(() -> clawOpenAndClose());
    }

    public Lambda setIntakeRotation(double position) {
        return new Lambda("setIntakeRotation")
                .setInit(() -> setRotation(position));
    }

}
*/
