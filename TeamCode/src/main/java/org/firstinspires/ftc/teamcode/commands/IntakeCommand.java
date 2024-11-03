package org.firstinspires.ftc.teamcode.commands;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Set;

import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem.*;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;

@Attach
public class IntakeCommand implements Command {
    IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    OutakeSubsystem outtakeSubsystem = new OutakeSubsystem();
    @Override
    public void initialise() {
        //TODO ADD SLIDE EXTENSION LAMBDA
        intakeSubsystem.intake(1);
    }

    @NonNull
    @Override
    public Set<Object> getRequirements() {
        return Collections.singleton(intakeSubsystem);
    }

    @NonNull
    @Override
    public Set<Wrapper.OpModeState> getRunStates() {
        return Collections.singleton(Wrapper.OpModeState.ACTIVE);
    }

    @Override
    public void end(boolean b) {
        new Parallel(
                outtakeSubsystem.reset(),
                //TODO ADD SLIDE RETRACTION LAMBDA
                intakeSubsystem.returnIntake()
        );


    }

    @Override
    public void execute() {

    }

    @Override
    public boolean finished() {
        return intakeSubsystem.distanceTripped();
    }
}
