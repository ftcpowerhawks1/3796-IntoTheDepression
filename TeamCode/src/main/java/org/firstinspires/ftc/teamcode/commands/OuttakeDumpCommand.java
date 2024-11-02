package org.firstinspires.ftc.teamcode.commands;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem.Attach;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;

@Attach
public class OuttakeDumpCommand implements Command {
    OutakeSubsystem outakeSubsystem = new OutakeSubsystem();
    VerticalSlides verticalSlides = new VerticalSlides();
    @Override
    public void initialise() {
        //TODO ADD SLIDE EXTENSION LAMBDA
    }

    @NonNull
    @Override
    public Set<Object> getRequirements() {
        Set<Object> returnset = new HashSet<>();
        returnset.add(outakeSubsystem);
        returnset.add(verticalSlides);
        return(returnset);
    }

    @NonNull
    @Override
    public Set<Wrapper.OpModeState> getRunStates() {
        return Collections.singleton(Wrapper.OpModeState.ACTIVE);
    }


    @Override
    public void execute() {}

    @Override
    public boolean finished() {
        return(verticalSlides.getVerticalControllerFinished());
    }

    @Override
    public void end(boolean b) {}
}
