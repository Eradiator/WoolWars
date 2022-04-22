package me.cubecrafter.woolwars.listeners;

import me.cubecrafter.woolwars.WoolWars;
import me.cubecrafter.woolwars.arena.Arena;
import me.cubecrafter.woolwars.arena.GameState;
import me.cubecrafter.woolwars.arena.Team;
import me.cubecrafter.woolwars.utils.GameUtil;
import me.cubecrafter.woolwars.utils.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!GameUtil.isPlaying(player)) return;
        Arena arena = GameUtil.getArenaByPlayer(player);
        if (arena.getBlocksRegion().isInside(e.getBlock().getLocation()) && arena.getGameState().equals(GameState.PLAYING)) {
            Team team = arena.getTeamByPlayer(player);
            if (e.getBlock().getType().toString().contains("WOOL")) {
                e.getBlock().setMetadata("woolwars", new FixedMetadataValue(WoolWars.getInstance(), team.getName()));
                arena.getPlayingTask().addPlacedBlock(team);
                arena.getPlayingTask().checkWinners();
            }
        } else if (arena.getArenaRegion().isInside(e.getBlock().getLocation())) {
            if (e.getBlock().getType().toString().contains("GLASS")) {
                arena.getPlacedBlocks().add(e.getBlock());
            } else {
                e.setCancelled(true);
                player.sendMessage(TextUtil.color("&cYou can't place blocks here!"));
            }
        }
    }

}