package org.firstinspires.ftc.teamcode.util.utilActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;

public class newMercurialAction implements Action {
    private final Command command;
    private boolean initialized = false;

    public newMercurialAction(Command command) {
        this.command = command;
    }

    @Override
    public synchronized boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            command.schedule();
            initialized = true;
        }
        boolean finished = initialized && !Mercurial.isScheduled(command);
        if (finished) {
            initialized = false;
        }
        return finished;
    }
}