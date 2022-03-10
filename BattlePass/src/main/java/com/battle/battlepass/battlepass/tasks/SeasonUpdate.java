package com.battle.battlepass.battlepass.tasks;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;

public class SeasonUpdate extends BukkitRunnable {


    @Override
    public void run() {

        LocalDate localDate = LocalDate.now();
        String unFormattedDate = localDate.toString();
        String[] parts = unFormattedDate.split("-"); //[year, month, day]

        int actualYear = Integer.parseInt(parts[0]);
        int actualMonth = Integer.parseInt(parts[1]);
        int actualDay = Integer.parseInt(parts[2]);


        String dataStartSeason = BattlePass.dataStartSeason; //[day, month, year]
        int duration = BattlePass.durationSeason;

        if (dataStartSeason==null) { return; }

        String[] partsStartSeason = dataStartSeason.split("/");

        int daySeasonStart = Integer.parseInt(partsStartSeason[0]);
        int monthSeasonStart = Integer.parseInt(partsStartSeason[1]);
        int yearSeasonStart = Integer.parseInt(partsStartSeason[2]);

        String finalDate = getMonthFinalSeason(monthSeasonStart,daySeasonStart, yearSeasonStart, duration);

        int monthFinalSeason = Integer.parseInt(finalDate.split("/")[1]);
        int yearFinalSeason = Integer.parseInt(finalDate.split("/")[2]);


        if (yearFinalSeason == yearSeasonStart) {
            if (actualDay == daySeasonStart) {

                if (actualMonth > monthSeasonStart && actualMonth <= monthFinalSeason) {

                    handle(duration);
                }

            }
        }

        else {

            if (actualDay == daySeasonStart) {


                if (actualYear == yearFinalSeason) {

                    int nextSeasonMonth = Math.min(Integer.parseInt(BattlePass.season) + 1, duration);

                    if (nextSeasonMonth != Integer.parseInt(BattlePass.season)) {

                        int month = getYearMonth(nextSeasonMonth, actualMonth);

                        int actualSeasonMonth = monthSeasonStart+Integer.parseInt(BattlePass.season)%12==0 ? 12 : monthSeasonStart+Integer.parseInt(BattlePass.season)%12;

                        if (month != actualSeasonMonth) {
                            handle(duration);
                        }

                    }

                }

            }

        }



    }

    private void handle(int duration) {
        int nextSeasonMonth = Math.min(Integer.parseInt(BattlePass.season) + 1, duration);

        if (nextSeasonMonth != Integer.parseInt(BattlePass.season)) {

            BattlePass.season = nextSeasonMonth+"";
            Utils.configSeasonMissions.getConfig().set("season", BattlePass.season);
            Utils.configSeasonMissions.saveConfig();
            Utils.reloadConfigs();

        }
    }


    private int getYearMonth(int increment, int actualMonth) {
        int number = actualMonth+increment;

        return number%12 == 0 ? 12 : number%12;
    }


    private String getMonthFinalSeason(int monthSeasonStart, int daySeasonStart, int yearSeasonStart,int duration) {
        int number = monthSeasonStart+duration;

        int lastMonth = number%12 == 0 ? 12 : number%12;
        int lastYear = yearSeasonStart;

        for (int i=0;i<duration;i++) {
            int increment = i+1;

            if (monthSeasonStart+increment>12) {
                monthSeasonStart = 1;
                lastYear++;
            }

        }

        return daySeasonStart+"/"+lastMonth+"/"+lastYear;
    }
}
