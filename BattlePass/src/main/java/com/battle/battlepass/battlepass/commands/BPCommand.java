package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class BPCommand implements CommandBase {

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inv = BattlePass.inventories.get("BattlePass").getInventory();

            Inventory invPlayer = Bukkit.createInventory(null, BattlePass.inventories.get("BattlePass").getInventory().getSize(), BattlePass.inventories.get("BattlePass").getInventory().getName());

            PlayerModel model = BattlePass.players.get(player.getUniqueId().toString());

            for (int i =0; i<inv.getSize();i++) {

                if (inv.getItem(i) != null && inv.getItem(i).getTypeId()!=0) {

                    ItemStack item = inv.getItem(i);
                    List<String> lore = item.getItemMeta().getLore();
                    ItemStack itemPlayer = new ItemBuilder(item, BattlePass.itemsActions.get(item.getItemMeta().getDisplayName()).getTarget(),  BattlePass.itemsActions.get(item.getItemMeta().getDisplayName()).getName()).getItem();

                    ItemMeta meta= itemPlayer.getItemMeta();
                    meta.setLore(lore);
                    itemPlayer.setItemMeta(meta);

                    invPlayer.setItem(i, itemPlayer);
                }
            }

            invPlayer.forEach(item -> {
                if (item!=null && item.getTypeId()!=0) {
                    List<String> lore = item.getItemMeta().getLore();

                    for (String loreItem : lore) {
                        int index = lore.indexOf(loreItem);

                        String nextTier = "Tier máximo alcançado.";

                        if (BattlePass.tiers.containsKey((Integer.parseInt(model.getTier())+1)+"")) {
                            nextTier = BattlePass.tiers.get((Integer.parseInt(model.getTier())+1)+"").getExp()+"";
                        }

                        String newLore= loreItem
                                .replace("{player}", player.getName())
                                .replace("{season}", BattlePass.season)
                                .replace("{tierPlayer}", model.getTier())
                                .replace("{expPlayer}", model.getExp()+"")
                                .replace("{NextExp}", nextTier);

                        lore.set(index, newLore);
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            });

            player.openInventory(invPlayer);
        }
    }
}
