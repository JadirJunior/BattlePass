package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.references.Tier;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTier implements CommandBase {

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("§1BattlePass >> §4Usage: /bpadmin settier <player> <tier>");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);

        if (player == null) {
            sender.sendMessage("§1BattlePass >> §4Player não está online");
            return;
        }

        Tier tier = BattlePass.tiers.get(args[2]);

        Bukkit.getConsoleSender().sendMessage(tier.getLevel()+"");
        if (tier != null) {
            int level = tier.getLevel();
            if (level<=BattlePass.tiers.size()) {
                if (tier.getCommands() != null) {
                    tier.getCommands().forEach(commandName -> {
                        Bukkit.dispatchCommand(sender, commandName.replace("{player}", player.getName()));
                    });
                }

                if (tier.getItem() != null){
                    player.getInventory().addItem(tier.getItem());
                }

                tier.setLevel(level);
                BattlePass.players.get(player.getUniqueId().toString()).setTier(level);
            }
        }
    }

}
