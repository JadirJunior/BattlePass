package com.battle.battlepass.battlepass.tasks;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import com.battle.battlepass.battlepass.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;

public class ResetDailyMissions extends BukkitRunnable {

    @Override
    public void run() {
        String last_update = BattlePass.last_update;

        if (last_update == null) { return; }

        LocalDate local = LocalDate.now();

        String unFormattedDate = local.toString();

        String[] parts = unFormattedDate.split("-"); //[year, month, day]

        int year = Integer.parseInt(parts[0]);

        int month = Integer.parseInt(parts[1]);

        int day = Integer.parseInt(parts[2]);

        String[] partsLastUpdate = last_update.split("/"); //[day, month, year]

        int dayUpdate = Integer.parseInt(partsLastUpdate[0]);

        int MonthUpdate = Integer.parseInt(partsLastUpdate[1]);

        int YearUpdate = Integer.parseInt(partsLastUpdate[2]);

        if (year != YearUpdate || day != dayUpdate || month != MonthUpdate) {

            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Resetando as missões diárias..."));

            Utils.config.getConfig().set("last_update", day+"/"+month+"/"+year);
            BattlePass.last_update = day+"/"+month+"/"+year;
            Utils.config.saveConfig();
            if (BattlePass.players == null || BattlePass.players.size()==0) return;
            BattlePass.players.forEach((uuid, playerModel) -> {
                playerModel.getMissionsDaily().forEach(mission -> {
                    if (mission.getStat() == Mission.StatsMission.COMPLETED) {
                        playerModel.getMissionsDaily().remove(mission);
                    }
                });
            });

        }

    }
}
