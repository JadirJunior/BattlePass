package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.references.Tier;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetExpCommand implements CommandBase {
    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        // bpadmin setExp <player> <exp>
        if (args.length != 3) {
            sender.sendMessage(ChatUtil.format("&4Usage: /bpadmin setExp <player> <exp>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);

        if (player == null) {
            sender.sendMessage(ChatUtil.format("&4Player Não está online."));
            return;
        }

        String exp = args[2];

        try {
            BattlePass.players.get(player.getUniqueId().toString()).setExp(Integer.parseInt(exp));
        } catch (Exception err) {
            err.printStackTrace();
            sender.sendMessage(ChatUtil.format("&4Ocorreu um erro inesperado, olhe no console para mais detalhes."));
        }

    }
}
