package me.cubecrafter.woolwars.arena.tasks;

import me.cubecrafter.woolwars.WoolWars;
import me.cubecrafter.woolwars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class ArenaTask {

    protected final Arena arena;
    private final BukkitTask task;

    public ArenaTask(Arena arena, int duration) {
        this.arena = arena;
        arena.setTimer(duration);
        task = Bukkit.getScheduler().runTaskTimer(WoolWars.getInstance(), () -> {
            if (arena.getTimer() == 0) {
                onEnd();
                cancel();
            } else {
                execute();
                arena.setTimer(arena.getTimer() - 1);
            }
        }, 20L, 20L);
        onStart();
    }

    public void cancel() {
        task.cancel();
    }

    public abstract void onStart();
    public abstract void execute();
    public abstract void onEnd();

}