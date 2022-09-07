package me.cubecrafter.woolwars.listeners;

import com.cryptomorin.xseries.XMaterial;
import me.cubecrafter.woolwars.arena.Arena;
import me.cubecrafter.woolwars.arena.GameState;
import me.cubecrafter.woolwars.config.Configuration;
import me.cubecrafter.woolwars.config.Messages;
import me.cubecrafter.woolwars.team.Team;
import me.cubecrafter.woolwars.utils.ArenaUtil;
import me.cubecrafter.woolwars.utils.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WoolListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!ArenaUtil.isPlaying(player)) return;
        Block block = e.getBlock();
        Arena arena = ArenaUtil.getArenaByPlayer(player);
        if (!arena.getGameState().equals(GameState.ACTIVE_ROUND) || arena.getDeadPlayers().contains(player)) {
            e.setCancelled(true);
            TextUtil.sendMessage(player, Messages.CANT_BREAK_BLOCK.getAsString());
        } else if (arena.getCenter().isInside(block.getLocation())) {
            if (arena.isCenterLocked()) {
                TextUtil.sendMessage(player, Messages.CENTER_LOCKED.getAsString());
                e.setCancelled(true);
                return;
            }
            arena.getRoundTask().removePlacedWool(block);
            block.setType(Material.AIR);
            arena.getRoundTask().addBrokenBlock(player);
        } else if (!arena.getPlacedBlocks().contains(block)) {
            e.setCancelled(true);
            TextUtil.sendMessage(player, Messages.CANT_BREAK_BLOCK.getAsString());
        } else {
            arena.getPlacedBlocks().remove(block);
            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena arena = ArenaUtil.getArenaByPlayer(player);
        if (arena == null) return;
        Block block = e.getBlock();
        if (!arena.getGameState().equals(GameState.ACTIVE_ROUND) || !arena.getArenaRegion().isInside(block.getLocation())) {
            e.setCancelled(true);
            TextUtil.sendMessage(player, Messages.CANT_PLACE_BLOCK.getAsString());
            return;
        }
        if (!ArenaUtil.isBlockInTeamBase(block, arena)) {
            if (block.getType().toString().contains("LAVA") || block.getType().toString().contains("WATER")) {
                e.setCancelled(true);
                TextUtil.sendMessage(player, Messages.CANT_PLACE_BLOCK.getAsString());
                return;
            }
            if (arena.getCenter().isInside(e.getBlock().getLocation())) {
                if (arena.isCenterLocked()) {
                    TextUtil.sendMessage(player, Messages.CENTER_LOCKED.getAsString());
                    e.setCancelled(true);
                    return;
                }
                if (block.getType().toString().endsWith("WOOL")) {
                    Team team = arena.getTeamByPlayer(player);
                    arena.getRoundTask().addPlacedWool(team, e.getBlock());
                    arena.getRoundTask().addPlacedWool(player);
                    arena.getRoundTask().checkWinners();
                }
                return;
            }
            for (String material : Configuration.PLACEABLE_BLOCKS.getAsStringList()) {
                if (block.getType() == XMaterial.matchXMaterial(material).get().parseMaterial()) {
                    arena.getPlacedBlocks().add(block);
                    return;
                }
            }
        }
        e.setCancelled(true);
        TextUtil.sendMessage(player, Messages.CANT_PLACE_BLOCK.getAsString());
    }

}