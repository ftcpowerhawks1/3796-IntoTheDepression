package org.firstinspires.ftc.teamcode.util.utilActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;

public class MercurialAction implements Action {
    private final Command command;
    private boolean initialized = false;
    private boolean secondCycle = false;
    public MercurialAction(Command command) {
        this.command = command;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        final boolean initialized = this.initialized;
        if (!initialized) {
            command.schedule();
            this.initialized = true;
        } 
        if (initialized && !Mercurial.isScheduled(command)){
            secondCycle = true;
        }
        final boolean finished = initialized && secondCycle && !Mercurial.isScheduled(command);
        if (finished) this.initialized = false;
        return finished;
    }
}