package com.battle.battlepass.battlepass.database;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Bukkit;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SavePlayerData {

    Connection connection;

    Statement statement;

    public SavePlayerData() {
        connection = new DatabaseConnection().getConnection();
        createTables();
    }

    private void savePlayerMissions() {
        PreparedStatement stm = null;
        ResultSet resultSet = null;

        try {
            for (String uuid : BattlePass.players.keySet()) {
                PlayerModel model = BattlePass.players.get(uuid);

                for (Mission missionDaily : model.getMissionsDaily()) {
                    stm = connection.prepareStatement("select * from playerMissions where uuid = ? and id_mission= ?");
                    stm.setString(1, uuid);
                    stm.setInt(2, missionDaily.getId());

                    resultSet = stm.executeQuery();

                    if (!resultSet.next()) {
                        stm = connection.prepareStatement("insert into playerMissions (uuid, id_mission) values (?, ?)");
                        stm.setString(1, uuid);
                        stm.setInt(2, missionDaily.getId());
                        stm.execute();
                    }
                }

                for (Mission missionSeason : model.getSeasonMissions()) {
                    stm = connection.prepareStatement("select * from playerMissions where uuid = ? and id_mission= ?");
                    stm.setString(1, uuid);
                    stm.setInt(2, missionSeason.getId());

                    resultSet = stm.executeQuery();

                    if (!resultSet.next()) {
                        stm = connection.prepareStatement("insert into playerMissions (uuid, id_mission) values (?, ?)");
                        stm.setString(1, uuid);
                        stm.setInt(2, missionSeason.getId());
                        stm.execute();
                    }

                }
            }
            if (stm != null) {
                stm.close();
            }

        } catch (Exception err) {
            err.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Ocorreu um erro ao salvar os dados no database."));
        }
    }

    private void saveMissions() {
        PreparedStatement stm = null;
        ResultSet resultSet = null;

        try {

            for (int id : BattlePass.missions.keySet()) {
                Mission mission = BattlePass.missions.get(id);

                stm = connection.prepareStatement("select * from missions where id = ?");
                stm.setInt(1, id);

                resultSet = stm.executeQuery();

                if (resultSet.next()) {

                    stm = connection.prepareStatement("update missions set status = ?, tipo=?");
                    stm.setString(1, mission.getStat() == Mission.StatsMission.ACTIVE ? "ACTIVE" : "COMPLETED");
                    stm.setString(2, mission.getTypeQuest() == TypeQuest.DAILY ? "DAILY" : "SEASON" );
                    stm.executeUpdate();

                } else {

                    stm = connection.prepareStatement("insert into missions (id, status, tipo) values (?,?,?)");
                    stm.setInt(1, id);
                    stm.setString(2, mission.getStat() == Mission.StatsMission.ACTIVE ? "ACTIVE" : "COMPLETED");
                    stm.setString(3, mission.getTypeQuest() == TypeQuest.DAILY ? "DAILY" : "SEASON");
                    stm.execute();

                }
            }

            for (int id : BattlePass.seasonMissions.keySet()) {
                Mission mission = BattlePass.seasonMissions.get(id);

                stm = connection.prepareStatement("select * from missions where id = ?");
                stm.setInt(1, id);

                resultSet = stm.executeQuery();

                if (resultSet.next()) {

                    stm = connection.prepareStatement("update missions set status = ?, tipo=?");
                    stm.setString(1, mission.getStat() == Mission.StatsMission.ACTIVE ? "ACTIVE" : "COMPLETED");
                    stm.setString(2, mission.getTypeQuest() == TypeQuest.DAILY ? "DAILY" : "SEASON" );
                    stm.executeUpdate();

                } else {

                    stm = connection.prepareStatement("insert into missions (id, status, tipo) values (?,?,?)");
                    stm.setInt(1, id);
                    stm.setString(2, mission.getStat() == Mission.StatsMission.ACTIVE ? "ACTIVE" : "COMPLETED");
                    stm.setString(3, mission.getTypeQuest() == TypeQuest.DAILY ? "DAILY" : "SEASON");
                    stm.execute();
                }
            }
            stm.close();
            savePlayerMissions();
        } catch (Exception err) {
            err.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Ocorreu um erro ao salvar as missões no database"));
        }
    }


    public void save() {
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        try {

            for (String uuid : BattlePass.players.keySet()) {
                PlayerModel model = BattlePass.players.get(uuid);

                stm = connection.prepareStatement("select * from player where uuid = ?");
                stm.setString(1, uuid);

                resultSet = stm.executeQuery();

                if (resultSet.next()) {

                    stm = connection.prepareStatement("update player set exp = ?, tier = ? where uuid=?");
                    stm.setInt(1, model.getExp());
                    stm.setString(2, model.getTier());
                    stm.setString(3, uuid);
                    stm.executeUpdate();

                } else {

                    stm = connection.prepareStatement("insert into player (uuid, exp, tier) values (?,?,?)");
                    stm.setString(1, uuid);
                    stm.setInt(2, model.getExp());
                    stm.setString(3, model.getTier());
                    stm.execute();

                }

                stm.close();
                saveMissions();
            }

        } catch (Exception err) {
            err.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Ocorreu um erro ao salvar a playerData no database."));
        }
    }

    private void createTables() {
        PreparedStatement stm = null;

        try {
            stm = connection.prepareStatement("create table if not exists player ( " +
                    "\nuuid varchar(200),\n" +
                    "exp int(20),\n" +
                    "tier varchar(20),\n" +
                    "primary key (uuid)\n" +
                    ");");

            stm.execute();

            stm = connection.prepareStatement("create table if not exists missions ( " +
                    "\nid int(20),\n" +
                    "status varchar(50) check (`status` = \"ACTIVE\" OR `status`=COMPLETED),\n" +
                    "primary key (id)\n" +
                    ");");

            stm.execute();

            stm = connection.prepareStatement("create table if not exists playerMissions (" +
                    "\nuuid varchar(200),\n" +
                    "id_mission int(20),\n" +
                    "foreign key (uuid) references player(uuid),\n" +
                    "foreign key (id_mission) references missions(id)\n" +
                    ");");

            stm.execute();

            stm.close();

        } catch(Exception err) {
            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Ocorreu um erro ao criar as tabelas do database."));
        }
    }

}
