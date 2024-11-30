package org.firstinspires.ftc.teamcode.commands;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;

public class intakeCommad implements Command {
    HorizontalSlides.SlideState slideState;

    public intakeCommad() {
    }

    @Override
    public void initialise() {
        HorizontalSlides.INSTANCE.runToPosition(-1500);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean b) {

    }

    @Override
    public boolean finished() {
        return true;
    }

    private final HashSet<Object> requirements = new HashSet<>(); {
        requirements.add(HorizontalSlides.INSTANCE);
    }

    @NonNull
    @Override
    public Set<Object> getRequirements() {
        return requirements;
    }

    @NonNull
    @Override
    public Set<Wrapper.OpModeState> getRunStates() {
        return Collections.singleton(Wrapper.OpModeState.ACTIVE);
    }
}
