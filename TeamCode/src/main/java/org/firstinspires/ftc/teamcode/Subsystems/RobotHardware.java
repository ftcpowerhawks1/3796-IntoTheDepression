package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMUNew;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.ImuParameters;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.dairy.core.util.features.BulkRead;


// lets add that BulkRead feature from Writing + Using Features
@BulkRead.Attach
public abstract class RobotHardware extends OpMode {
    // lets add OpModeLazyCells for each hardware item
    private final OpModeLazyCell<DcMotorEx> leftFront
            = new OpModeLazyCell<>(() -> hardwareMap.get(CachingDcMotorEx.class, "lf"));
    private final OpModeLazyCell<DcMotorEx> leftBack
            = new OpModeLazyCell<>(() -> hardwareMap.get(CachingDcMotorEx.class, "lb"));
    // right motors need some additional setup
    private final OpModeLazyCell<DcMotorEx> rightBack = new OpModeLazyCell<>(() -> {
        DcMotorEx m = hardwareMap.get(CachingDcMotorEx.class, "rb");
        m.setDirection(DcMotorSimple.Direction.REVERSE);
        return m;
    });
    private final OpModeLazyCell<DcMotorEx> rightFront = new OpModeLazyCell<>(() -> {
        DcMotorEx m = hardwareMap.get(CachingDcMotorEx.class, "rf");
        m.setDirection(DcMotorSimple.Direction.REVERSE);
        return m;
    });
    private final OpModeLazyCell<IMU> hardwareimu = new OpModeLazyCell<>(() -> {
        IMU gyro = hardwareMap.get(IMU.class,"imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        gyro.initialize(parameters);
        return gyro;
    });


    // and lets add getters to access the cells
    public DcMotorEx getLeftFront() { return leftFront.get(); }
    public DcMotorEx getLeftBack() { return leftBack.get(); }
    public DcMotorEx getRightBack() { return rightBack.get(); }
    public DcMotorEx getRightFront() { return rightFront.get(); }

    public IMU getIMU() { return hardwareimu.get();}


    public enum Alliance {
        RED,
        BLUE
    }

    public enum Side {
        LEFT,
        RIGHT
    }

    public enum LeftAutonState {
        PRELOAD,
        RIGHTSAMPLE,
        SCORERIGHTSAMPLE,
        MIDDLESAMPLE,
        SCOREMIDDLESAMPLE,
        LEFTSAMPLE,
        SCORELEFTSAMPLE,
        PICKUPCYCLE,
        SCORECYCLE,
        PARK,
        IDLE
    }
}
