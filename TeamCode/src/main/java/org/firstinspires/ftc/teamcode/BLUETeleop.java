package org.firstinspires.ftc.teamcode;


import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach
@IntakeSubsystem.Attach
public class BLUETeleop extends RobotHardware {

    @Override
    public void init() {
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    }

    @Override
    public void loop() {
        Mercurial.gamepad1().a().whileTrue(new Sequential(new Lambda("intake").then(new Lambda("return-intake"))));
    }
}
