package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import com.battle.battlepass.battlepass.utils.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PInventoryClick implements Listener {

    public void handle(InventoryClickEvent e, String itemName, Player player, HashMap<String, InventoryBuilder> hash) {
        InventoryBuilder invBuilder = hash.get(player.getUniqueId().toString());

        if (itemName.equalsIgnoreCase(invBuilder.pagSeg)) {
            invBuilder.changePage(invBuilder.getPage() + 1, player);
        }

        else if (itemName.equalsIgnoreCase(invBuilder.pagAnterior)) {
            invBuilder.changePage(invBuilder.getPage() - 1, player);
        }

        if (invBuilder.getItemsActions().containsKey(e.getSlot())) {

            String command = invBuilder.getItemsActions().get(e.getSlot()).getCommmand();

            if (!command.equalsIgnoreCase("")) {

                if (e.getInventory().getName().equalsIgnoreCase(BattlePass.dailyInvPlayer.get(player.getUniqueId().toString()).getInventory().getName()) ||
                        e.getInventory().getName().equalsIgnoreCase(BattlePass.seasonInvPlayer.get(player.getUniqueId().toString()).getInventory().getName())) {

                    if (!e.getCurrentItem().getItemMeta().getLore().get(0).equalsIgnoreCase(invBuilder.notInit)) {
                        return;
                    }

                    ItemMeta meta = e.getCurrentItem().getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(invBuilder.active);
                    e.getCurrentItem().getItemMeta().getLore().forEach(itemLore -> {
                        if (e.getCurrentItem().getItemMeta().getLore().indexOf(itemLore) != 0) {
                            lore.add(ChatUtil.format(itemLore));
                        }
                    });
                    meta.setLore(lore);
                    e.getCurrentItem().setItemMeta(meta);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
                    return;
                }

                ItemMeta meta = e.getCurrentItem().getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(invBuilder.alreadyTier);
                meta.setLore(lore);
                e.getCurrentItem().setItemMeta(meta);
                e.getCurrentItem().getData().setData((byte) 5);
                e.getCurrentItem().setTypeId(160);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
            }
        }
    }


    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if (BattlePass.inventories.get(e.getInventory().getName()) != null) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            if (e.getCurrentItem().getTypeId() == 0) {
                return;
            }

            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();

            if (BattlePass.itemsActions.containsKey(itemName) || BattlePass.seasonInvPlayer.containsKey(player.getUniqueId().toString())) {
                String target = BattlePass.itemsActions.containsKey(itemName) ? BattlePass.itemsActions.get(itemName).getTarget() : "";

                if (target.equalsIgnoreCase("")) {
                    if (e.getInventory().getName().equalsIgnoreCase(BattlePass.seasonInvPlayer.get(player.getUniqueId().toString()).getInventory().getName())) {
                        handle(e, itemName, player, BattlePass.seasonInvPlayer);
                    }

                    else if (e.getInventory().getName().equalsIgnoreCase(BattlePass.dailyInvPlayer.get(player.getUniqueId().toString()).getInventory().getName())) {
                        handle(e,itemName,player, BattlePass.dailyInvPlayer);
                    }

                    else if (e.getInventory().getName().equalsIgnoreCase(BattlePass.tierInvPlayer.get(player.getUniqueId().toString()).getInventory().getName())) {
                        handle(e,itemName,player,BattlePass.tierInvPlayer);
                    }

                    return;
                }

                if (target.equalsIgnoreCase("tier")) {

                    player.openInventory(BattlePass.tierInvPlayer.get(player.getUniqueId().toString()).getInventory(player));
                    return;

                }

                else if (target.equalsIgnoreCase("daily-missions")) {

                    player.openInventory(BattlePass.dailyInvPlayer.get(player.getUniqueId().toString()).getInventory(player));
                    return;

                }

                else if (target.equalsIgnoreCase("season-missions")) {
                    player.openInventory(BattlePass.seasonInvPlayer.get(player.getUniqueId().toString()).getInventory(player));
                    return;

                }

                player.openInventory(BattlePass.inventories.get(target).getInventory());
            }
        }
    }
}
