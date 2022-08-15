package me.cubecrafter.woolwars.listeners;

import me.cubecrafter.woolwars.arena.Arena;
import me.cubecrafter.woolwars.arena.GameState;
import me.cubecrafter.woolwars.menu.Menu;
import me.cubecrafter.woolwars.menu.MenuItem;
import me.cubecrafter.woolwars.utils.ArenaUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Map;
import java.util.function.Consumer;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        Player player = (Player) e.getWhoClicked();
        Arena arena = ArenaUtil.getArenaByPlayer(player);
        if (arena != null) {
            if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                e.setCancelled(true);
                return;
            }
            if (e.getInventory().getType().equals(InventoryType.CRAFTING) && !arena.getGameState().equals(GameState.PRE_ROUND) && !arena.getGameState().equals(GameState.ACTIVE_ROUND)) {
                e.setCancelled(true);
                return;
            }
        }
        if (e.getInventory().getHolder() instanceof Menu) {
            e.setCancelled(true);
            Menu menu = (Menu) e.getInventory().getHolder();
            MenuItem item = menu.getItems().get(e.getSlot());
            if (item == null) return;
            for (Map.Entry<Consumer<InventoryClickEvent>, ClickType[]> entry : item.getActions().entrySet()) {
                for (ClickType clickType : entry.getValue()) {
                    if (e.getClick().equals(clickType)) {
                        entry.getKey().accept(e);
                        menu.updateMenu();
                    }
                }
            }
        }


    }

}
