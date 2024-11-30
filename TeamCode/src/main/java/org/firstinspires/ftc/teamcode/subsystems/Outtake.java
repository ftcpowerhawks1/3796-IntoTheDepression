package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants.OutakeConst;

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
    private final Cell<CachingServo> outtakePivotLeft = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, OutakeConst.outtakePivotLeft)));
    private final Cell<CachingServo> outtakePivotRight = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, OutakeConst.outtakePivotRight)));

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
                outtakePivotRight.get().setPosition(OutakeConst.scoringPos);
                outtakePivotLeft.get().setPosition(OutakeConst.scoringPos);
                break;
            case IN:
                outtakePivotRight.get().setPosition(OutakeConst.homePos);
                outtakePivotLeft.get().setPosition(OutakeConst.homePos);
                break;

        }
        Outtake.outtakePivotState = outtakePivotState;
    }

    public Lambda setOuttakePivot(Outtake.OuttakePivotState outtakePivotState) {
        return new Lambda("setOuttakePivot")
                .setInit(() -> setPivot(outtakePivotState));
    }
}
