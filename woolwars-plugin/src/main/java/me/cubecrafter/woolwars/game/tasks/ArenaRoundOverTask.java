package me.cubecrafter.woolwars.game.tasks;

import me.cubecrafter.woolwars.game.arena.Arena;
import me.cubecrafter.woolwars.game.arena.GameState;
import me.cubecrafter.woolwars.game.powerup.PowerUp;

public class ArenaRoundOverTask extends ArenaTask {

    public ArenaRoundOverTask(Arena arena) {
        super(arena);
        arena.getPowerUps().forEach(PowerUp::remove);
    }

    @Override
    public void execute() {

    }

    @Override
    public void onTimerEnd() {
        arena.setGameState(GameState.PRE_ROUND);
    }

    @Override
    public int getTaskDuration() {
        return 5;
    }

}