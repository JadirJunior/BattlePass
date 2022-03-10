package com.battle.battlepass.battlepass.commands;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.CommandBase;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import com.battle.battlepass.battlepass.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandBase {

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        sender.sendMessage(ChatUtil.format("&4Recarregando arquivos de configuração..."));

        Bukkit.getScheduler().cancelAllTasks();

        Utils.savePlayers();
        Utils.reloadConfigs();

         sender.sendMessage(ChatUtil.format("&bArquivos recarregados..."));
    }

}
