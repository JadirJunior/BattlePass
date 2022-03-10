package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetExpCommand implements CommandBase {
    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        //bpadmin getExp <player>

        if (args.length != 2) {
            sender.sendMessage(ChatUtil.format("&4Usage: /bpadmin getExp <player>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);

        if (player==null) {
            sender.sendMessage(ChatUtil.format("&4Player não encontrado"));
            return;
        }
        try {
            sender.sendMessage(ChatUtil.format("&bExp de &5"+player.getName()+"&b -> "+BattlePass.players.get(player.getUniqueId().toString()).getExp()));
        } catch (Exception err) {
            sender.sendMessage(ChatUtil.format("&4Ocorreu um erro ao pegar o xp. Tente novamente."));
        }

    }
}
