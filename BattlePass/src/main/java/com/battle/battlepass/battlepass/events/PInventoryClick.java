package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.Tier;
import com.battle.battlepass.battlepass.utils.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PInventoryClick implements Listener {
    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if (BattlePass.inventories.get(e.getInventory().getName()) != null) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getTypeId() == 0) {
                return;
            }
            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            if (BattlePass.itemsActions.containsKey(itemName)) {
                String target = BattlePass.itemsActions.get(itemName).getTarget();
                if (target.equalsIgnoreCase("")) {return;}
                if (target.equalsIgnoreCase("tier")) {
                    BattlePass.inventories.get(target).getInventory().forEach(item -> {
                        if (item == null) return;
                        String tierName = item.getItemMeta().getDisplayName().replace("Tier ", "");

                        BattlePass.players.forEach((uuid, model) -> {
                            Tier tier = BattlePass.tiers.get(tierName);
                            int expTier = tier.getExp();

                            List<String> lore = new ArrayList<>();
                            ItemMeta meta = item.getItemMeta();

                            if (expTier>model.getExp()) {
                                lore.add("§4Você precisa de §b"+expTier+"§4 para este Tier");
                                meta.setDisplayName(item.getItemMeta().getDisplayName());
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            }

                            else {
                                lore.add("§bVocê possui o exp necessário para subir de tier");
                                meta.setDisplayName(item.getItemMeta().getDisplayName());
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            }
                        });
                    });
                    player.openInventory(BattlePass.inventories.get(target).getInventory());
                    return;
                }
                player.openInventory(BattlePass.inventories.get(target).getInventory());
            }
        }
    }
}
