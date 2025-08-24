package dev.tqqn.modules.game.framework.tasks;

import dev.tqqn.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public final class StartCountdownTask extends BukkitRunnable {

    private int startCountdown = 31;
    private static final String message = "<red>Game is starting in <white><bold>%s<reset><red>s.";

    private boolean isStopped = false;

    @Override
    public void run() {
        if (isStopped) {
            broadcast("<red>Game start cancelled. Not enough players.");
            cancel();
            return;
        }

        if (startCountdown == 31) {
            broadcast(String.format(message, startCountdown-1));
        }

        if (startCountdown == 10) {
            broadcast(String.format(message, startCountdown));
        }

        if (startCountdown < 6) {
            broadcast(String.format(message, startCountdown));
        }

        if (startCountdown < 1) {
            cancel();
            return;
        }
        startCountdown--;
    }

    public void stopTask() {
        this.isStopped = true;
    }

    private void broadcast(String message) {
        Bukkit.broadcast(ChatUtils.format(message));
    }
}
