package com.battle.battlepass.battlepass.manager;

import com.battle.battlepass.battlepass.commands.*;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("bp")) {
            if (args.length == 0) {
                new BPCommand().execute(sender, command, args);
            }
        } else if (command.getName().equalsIgnoreCase("startquest")) {
            new StartQuestCommand().execute(sender, command, args);
        } else if (command.getName().equalsIgnoreCase("bpadmin")) {

            if (args.length == 0) {
                sender.sendMessage(ChatUtil.format("&bBattlePass Plugin"));
                sender.sendMessage(ChatUtil.format("&b----------------------------------"));
                sender.sendMessage(ChatUtil.format("&b/bpadmin getExp <player> -> Pega o xp de um player"));
                sender.sendMessage(ChatUtil.format("&b/bpadmin setExp <player <exp> -> Seta o xp do player"));
                sender.sendMessage(ChatUtil.format("&b/bpadmin setTier <player> <tier> -> Seta o tier de um player"));
                sender.sendMessage(ChatUtil.format("&b/bpadmin getTier <player> -> Pega o tier de um player"));
                sender.sendMessage(ChatUtil.format("&b/bpadmin reload -> Recarrega as configurações e salva os players."));
                sender.sendMessage(ChatUtil.format("&b/bpadmin setPremium <player> <true:false> -> Seta o passe premium em um player."));
                sender.sendMessage(ChatUtil.format("&b----------------------------------"));
                sender.sendMessage(ChatUtil.format("&bDeveloped by Jadir"));
                return false;
            }

            if (args[0].equalsIgnoreCase("settier")) {
                new SetTier().execute(sender, command, args);
            } else if (args[0].equalsIgnoreCase("setexp")) {
                new SetExpCommand().execute(sender, command, args);
            } else if (args[0].equalsIgnoreCase("getexp")) {
                new GetExpCommand().execute(sender, command, args);
            } else if (args[0].equalsIgnoreCase("getTier")) {
                new GetTierCommand().execute(sender, command, args);
            } else if (args[0].equalsIgnoreCase("reload")) {
                new ReloadCommand().execute(sender, command, args);
            } else if (args[0].equalsIgnoreCase("setPremium")) {
                new SetPremiumCommand().execute(sender, command, args);
            }
        }

        return false;
    }
}
