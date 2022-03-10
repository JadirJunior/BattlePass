package com.battle.battlepass.battlepass.utils;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryBuilder {

    private Inventory inv;
    private TypeQuest typeQuest;

    private List<String> items = new ArrayList<>();
    private HashMap<Integer, ItemBuilder> itemsActions = new HashMap<>();

    private int page = 1;
    private int maxItems = 36;

    public String pagAnterior = ChatUtil.format("&8&lPagina Anterior");
    public String pagSeg = ChatUtil.format("&8&lPagina Seguinte");
    public String notInit = ChatUtil.format("&7Não Iniciada.");
    public String active = ChatUtil.format("&aMissão Ativa.");
    public String completed = ChatUtil.format("&bMissão Completa.");

    public String alreadyTier = ChatUtil.format("Voce ja esta nesse Tier");

    public void changePage(int page, Player player) {
        this.page = page<=0 ? 1 : page;
        player.openInventory(getInventory(player));
    }


    public InventoryBuilder(int size, String name, TypeQuest typeQuest) {
        this.inv = Bukkit.createInventory(null, size, name);
        this.typeQuest = typeQuest;
    }

    public InventoryBuilder(int size, String name) {
        this.inv = Bukkit.createInventory(null, size, name);
        this.typeQuest = null;
    }

    public Inventory getInventory() {
        return inv;
    }

    public Inventory getInventory(Player player) {
        inv.clear();
        items.clear();
        itemsActions.clear();
        AtomicInteger index = new AtomicInteger();
        AtomicInteger contador = new AtomicInteger();
        index.set(0);

        //QUANDO A MISSÃO FOR DIÁRIA
        if (typeQuest == TypeQuest.DAILY) {



            BattlePass.missions.forEach((id, mission) -> {

                ItemBuilder builder = new ItemBuilder(mission.getName(), 278, 0);
                PlayerModel model = BattlePass.players.get(player.getUniqueId().toString());


                if (
                        (model.isPremium() && mission.isPremium())
                                || (!model.isPremium() && !mission.isPremium()) ||
                                (model.isPremium() && !mission.isPremium())) {

                    //PERCORE AS MISSÕES DO PLAYER E VÊ SE ESTÁ ATIVA OU COMPLETA.
                    model.getMissionsDaily().forEach(daily -> {

                        if (daily.getId() == id) {

                            if (daily.getStat() == Mission.StatsMission.ACTIVE) {
                                builder.addLore(active);
                                for (String desc: mission.getDescription() ) {
                                    builder.addLore(ChatUtil.format(desc));
                                }
                            }

                            else if (daily.getStat() == Mission.StatsMission.COMPLETED) {
                                builder.addLore(completed);
                                for (String desc: mission.getDescription() ) {
                                    builder.addLore(ChatUtil.format(desc));
                                }
                            }

                            items.add(builder.getName());

                            if (items.size() >= maxItems) {
                                return;
                            }

                            inv.setItem(index.get(),builder.getItem());

                        }
                    });

                    //CASO O PLAYER NÃO TENHA A MISSÃO
                    if (!items.contains(mission.getName())) {

                        builder.addLore(notInit);
                        for (String desc: mission.getDescription() ) {
                            builder.addLore(ChatUtil.format(desc));
                        }
                        builder.setCommand("startquest {player} daily "+mission.getId());
                        this.itemsActions.put(index.get() ,builder);
                        items.add(builder.getName());

                        if (items.size() >= maxItems) {
                            return;
                        }

                        inv.setItem(index.get(), builder.getItem());
                    }

                    index.getAndIncrement();
                }


            });

            BattlePass.dailyInvPlayer.remove(player.getUniqueId().toString());
            BattlePass.dailyInvPlayer.put(player.getUniqueId().toString(), this);
        }

        //QUANDO A MISSÃO FOR DE TEMPORADA
        if (typeQuest == TypeQuest.SEASON) {

            ItemBuilder pagAnt = new ItemBuilder(pagAnterior,262, 0);
            ItemBuilder pagSeguinte = new ItemBuilder(pagSeg,262, 0);

            inv.setItem(BattlePass.previuousPageSlot, pagAnt.getItem());
            inv.setItem(BattlePass.pageSlot, new ItemBuilder(ChatUtil.format("&b&lPage " + page), 339, 0).getItem());
            inv.setItem(BattlePass.nextPageSlot, pagSeguinte.getItem());

            if (!BattlePass.itemsActions.containsKey(pagAnt.getName()) && !BattlePass.itemsActions.containsKey(pagSeguinte.getName())) {
                BattlePass.itemsActions.put(pagAnterior, pagAnt);
                BattlePass.itemsActions.put(pagSeg, pagSeguinte);
            }

            BattlePass.seasonMissions.forEach((id, mission) -> {


                PlayerModel model =BattlePass.players.get(player.getUniqueId().toString());

                if ((model.isPremium() && mission.isPremium()) || (!model.isPremium() && !mission.isPremium()) || (model.isPremium() && !mission.isPremium())) {
                    contador.getAndIncrement();

                    if (contador.get()>=maxItems*(page-1)) {

                        ItemBuilder builder = new ItemBuilder(mission.getName(), 278, 0);

                        model.getSeasonMissions().forEach(season -> {

                            if (season.getId() == id && season.getSeason().equalsIgnoreCase(mission.getSeason())) {

                                if (season.getStat() == Mission.StatsMission.ACTIVE) {
                                    builder.addLore(active);
                                    for (String desc: mission.getDescription() ) {
                                        builder.addLore(ChatUtil.format(desc));
                                    }
                                }

                                else if (season.getStat() == Mission.StatsMission.COMPLETED) {
                                    builder.addLore(completed);
                                    for (String desc: mission.getDescription() ) {
                                        builder.addLore(ChatUtil.format(desc));
                                    }
                                }

                                items.add(builder.getName());

                                if (items.size() >= maxItems) { return; }

                                inv.setItem(index.get(), builder.getItem());
                            }

                        });

                        if (!items.contains(mission.getName())) {

                            builder.addLore(notInit);
                            for (String desc: mission.getDescription() ) {
                                builder.addLore(ChatUtil.format(desc));
                            }

                            builder.setCommand("startquest {player} season "+mission.getId());
                            items.add(builder.getName());
                            this.itemsActions.put(index.get() ,builder);

                            if (items.size() >= maxItems) {
                                return;
                            }

                            inv.setItem(index.get(), builder.getItem());

                        }

                    }

                    index.getAndIncrement();
                }


          });

            BattlePass.seasonInvPlayer.remove(player.getUniqueId().toString());
            BattlePass.seasonInvPlayer.put(player.getUniqueId().toString(), this);
        }

        //QUANDO NÃO TIVER TIPO (QUANDO FOR DE TIER)
        if (typeQuest == null) {

            ItemBuilder pagAnt = new ItemBuilder(pagAnterior,262, 0);
            ItemBuilder pagSeguinte = new ItemBuilder(pagSeg,262, 0);

            inv.setItem(BattlePass.previuousPageSlot, pagAnt.getItem());
            inv.setItem(BattlePass.pageSlot, new ItemBuilder(ChatUtil.format("&b&lPage " + page), 339, 0).getItem());
            inv.setItem(BattlePass.nextPageSlot, pagSeguinte.getItem());


            PlayerModel model = BattlePass.players.get(player.getUniqueId().toString());


            BattlePass.tiers.forEach((tierName, tier) -> {

                if ((model.isPremium() && tier.isPremium()) || (!model.isPremium() && !tier.isPremium()) || (model.isPremium() && !tier.isPremium())) {
                    contador.getAndIncrement();

                    if (contador.get()>=maxItems*(page-1)) {
                        ItemBuilder builder = tier.isPremium() ? new ItemBuilder(tier.getName(), 160, 1) : new ItemBuilder(tier.getName(), 160, 7);
                        int expTier = tier.getExp();
                        int tierLevel = tier.getLevel();

                        if (expTier > model.getExp()) {
                            builder.addLore("§4Você precisa de §b" + expTier + "§4 para este Tier");
                            inv.addItem(builder.getItem());
                        }
                        else {
                            int level = Integer.parseInt(model.getTier());

                            if (level < tierLevel) {
                                builder.addLore("§bVocê possui o exp necessário para subir de tier");
                                builder.addLore(ChatUtil.format("&4Clique para subir de Tier"));
                                inv.addItem(builder.getItem());
                                int newLevel = level+1;
                                builder.setCommand("bpadmin settier {player} "+newLevel);
                                this.itemsActions.put(index.get(), builder);
                            }

                            else {
                                builder.addLore(alreadyTier);
                                builder.getItem().setTypeId(160);
                                builder.getItem().getData().setData((byte) 5);
                                inv.addItem(builder.getItem());
                            }
                        }
                        index.getAndIncrement();
                    }
                }

            });

            BattlePass.tierInvPlayer.remove(player.getUniqueId().toString());
            BattlePass.tierInvPlayer.put(player.getUniqueId().toString(), this);
        }

        contador.set(0);
        index.set(0);
        return inv;
    }

    public HashMap<Integer,ItemBuilder> getItemsActions() { return itemsActions; }


    public int getPage() {
        return page;
    }




}
