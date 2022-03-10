package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetPremiumCommand implements CommandBase {
    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatUtil.format("&4Usage: /bpadmin setPremium <player> <true:false>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);

        OfflinePlayer playerOff = null;

        if (player ==null) {
            playerOff = Bukkit.getOfflinePlayer(args[1]);
        }

        if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
            sender.sendMessage(ChatUtil.format("&4Apenas true ou false que vale!"));
            return;
        }

        if ((player != null && BattlePass.players.containsKey(player.getUniqueId().toString())) || (playerOff!= null && BattlePass.players.containsKey(playerOff.getUniqueId().toString()))) {
            PlayerModel model = player == null ? BattlePass.players.get(playerOff.getUniqueId().toString()) : BattlePass.players.get(player.getUniqueId().toString());

            if (player != null || playerOff != null) {
                boolean isPremium = args[2].equalsIgnoreCase("true");
                model.setIsPremium(isPremium);
                sender.sendMessage(ChatUtil.format("&bPasse do player setado para " + isPremium));
                return;
            }
        }






    }
}