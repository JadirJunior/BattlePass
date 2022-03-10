package com.battle.battlepass.battlepass.tasks;

import com.battle.battlepass.battlepass.utils.ChatUtil;
import com.battle.battlepass.battlepass.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


public class AutoSavePlayers extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&bSalvando dados dos players..."));
        Utils.savePlayers();
        Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&bDados salvos."));
    }

}
