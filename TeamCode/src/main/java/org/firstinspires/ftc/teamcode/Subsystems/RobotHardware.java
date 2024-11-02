package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.dairy.core.util.supplier.logical.EnhancedBooleanSupplier;
import dev.frozenmilk.dairy.pasteurized.Pasteurized;

// lets add that BulkRead feature from Writing + Using Features
@BulkRead.Attach
public abstract class RobotHardware extends OpMode {
    // lets add OpModeLazyCells for each hardware item
    private final OpModeLazyCell<DcMotorEx> leftFront
            = new OpModeLazyCell<>(() -> hardwareMap.get(DcMotorEx.class, "lf"));
    private final OpModeLazyCell<DcMotorEx> leftBack
            = new OpModeLazyCell<>(() -> hardwareMap.get(DcMotorEx.class, "lb"));
    // right motors need some additional setup
    private final OpModeLazyCell<DcMotorEx> rightBack = new OpModeLazyCell<>(() -> {
        DcMotorEx m = hardwareMap.get(DcMotorEx.class, "rb");
        m.setDirection(DcMotorSimple.Direction.REVERSE);
        return m;
    });
    private final OpModeLazyCell<DcMotorEx> rightFront = new OpModeLazyCell<>(() -> {
        DcMotorEx m = hardwareMap.get(DcMotorEx.class, "rf");
        m.setDirection(DcMotorSimple.Direction.REVERSE);
        return m;
    });

    // and lets add getters to access the cells
    public DcMotorEx getLeftFront() { return leftFront.get(); }
    public DcMotorEx getLeftBack() { return leftBack.get(); }
    public DcMotorEx getRightBack() { return rightBack.get(); }
    public DcMotorEx getRightFront() { return rightFront.get(); }


    //Gamepad Buttons
    public EnhancedBooleanSupplier intakeCommand = Pasteurized.gamepad1().a();
    public EnhancedBooleanSupplier intakeCancelCommand = Pasteurized.gamepad1().b();

}
