package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class Outtake extends SDKSubsystem {
    public static final Outtake INSTANCE = new Outtake();
    private Outtake() {}

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
    //Declare states
    public enum OuttakePivotState {
        OUT,
        IN
    }
    public static OuttakePivotState outtakePivotState;

    //Hardware
    private final Cell<CachingServo> outtakePivotLeft = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Outtake.outtakePivotLeft)));
    private final Cell<CachingServo> outtakePivotRight = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Outtake.outtakePivotRight)));

    public void setPivotPosition(double position) {
        outtakePivotLeft.get().setPosition(position);
        outtakePivotRight.get().setPosition(position);
    }
    public double getPivotPosition() {
        return outtakePivotLeft.get().getPosition();
    }

    public void setPivot(OuttakePivotState outtakePivotState){
        switch (outtakePivotState) {
            case OUT:
                setPivotPosition(Constants.Outtake.outPos);
                break;
            case IN:
                setPivotPosition(Constants.Outtake.inPos);
                break;

        }
        Outtake.outtakePivotState = outtakePivotState;
    }

    public Lambda setOuttakePivot(Outtake.OuttakePivotState outtakePivotState) {
        return new Lambda("setOuttakePivot")
                .setInit(() -> setPivot(outtakePivotState));
    }
}
