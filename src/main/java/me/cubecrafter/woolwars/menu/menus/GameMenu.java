package me.cubecrafter.woolwars.menu.menus;

import me.cubecrafter.woolwars.menu.Menu;
import me.cubecrafter.woolwars.menu.MenuItem;
import me.cubecrafter.woolwars.utils.GameUtil;
import me.cubecrafter.woolwars.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMenu extends Menu {

    public GameMenu(Player player) {
        super(player);
    }

    @Override
    public String getTitle() {
        return "● Play Wool Wars";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public List<MenuItem> getItems() {
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem(22, new ItemBuilder("WOODEN_SWORD").setDisplayName("&dWool Wars").setLore(Arrays.asList("&eClick to play!", "&7{total_players} currently playing!")).build()).addAction(e -> {
            closeMenu();
            GameUtil.joinRandom(player);
        }, ClickType.LEFT, ClickType.RIGHT).setClickSound("UI_BUTTON_CLICK"));

        items.add(new MenuItem(39, new ItemBuilder("CLOCK").setDisplayName("&aAvailable Arenas").setLore(Arrays.asList("&eClick to browse!")).build()).addAction(e -> {
            new ArenaListMenu(player).openMenu();
        }, ClickType.LEFT, ClickType.RIGHT).setClickSound("UI_BUTTON_CLICK"));

        items.add(new MenuItem(40, new ItemBuilder("BARRIER").setDisplayName("&cClose").build()).addAction(e -> {
            closeMenu();
        }, ClickType.LEFT, ClickType.RIGHT).setClickSound("UI_BUTTON_CLICK"));

        items.add(new MenuItem(41, new ItemBuilder("PAPER").setDisplayName("&aStatistics").setLore(Arrays.asList("")).build()).addAction(e -> {
            player.sendMessage("stats");
        }, ClickType.LEFT, ClickType.RIGHT).setClickSound("UI_BUTTON_CLICK"));

        return items;
    }

    @Override
    public boolean update() {
        return false;
    }

}
