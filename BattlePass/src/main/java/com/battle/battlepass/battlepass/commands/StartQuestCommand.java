package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.references.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.Serializable;

public class StartQuestCommand implements CommandBase {

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("§1BattlePass >> §4Usage: /startquest <player> <type (season:daily)> <id>");
            return;
        }
        String playerName = args[0];
        int id = 0;
        try {
            id = Integer.parseInt(args[2]);
        } catch (Exception err) { sender.sendMessage("O ID informado deve ser um numero!"); return; }

        Player player = Bukkit.getPlayer(playerName);
        String type = args[1];
        if (player == null) {
            sender.sendMessage("Player nao esta online.");
            return;
        }

        String uuid = player.getUniqueId().toString();

        PlayerModel playerModel = BattlePass.players.get(uuid);

        TypeQuest typeQuest = null;

        if (type.equalsIgnoreCase("season")) {
            typeQuest = TypeQuest.SEASON;
        } else if (type.equalsIgnoreCase("daily")) {
            typeQuest = TypeQuest.DAILY;
        }

        Serializable ser = playerModel.startQuest(id, typeQuest);

        if (ser == null) {
            sender.sendMessage("§1BattlePass >> §4Quest não existe");
        } else if (ser.equals(false)) {
            sender.sendMessage("§1BattlePass >> §4Player informado já possui a quest ativa ou terminada.");
        }
    }
}
