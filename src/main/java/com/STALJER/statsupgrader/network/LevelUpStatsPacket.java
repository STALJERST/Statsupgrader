package com.STALJER.statsupgrader.network;

import com.STALJER.statsupgrader.StatsSystem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class LevelUpStatsPacket {
    public LevelUpStatsPacket() {}

    public LevelUpStatsPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                int currentStatsLevel = player.getPersistentData().contains(StatsSystem.NBT_STATS_LEVEL)
                        ? player.getPersistentData().getInt(StatsSystem.NBT_STATS_LEVEL) : 1;

                int cost = StatsSystem.getLevelCost(currentStatsLevel);

                if (currentStatsLevel < com.STALJER.statsupgrader.Config.MAX_STATS_LEVEL.get()) {
                    if (player.experienceLevel >= cost) {
                        player.giveExperienceLevels(-cost);
                        int currentPoints = player.getPersistentData().getInt(StatsSystem.NBT_STAT_POINTS);

                        player.getPersistentData().putInt(StatsSystem.NBT_STATS_LEVEL, currentStatsLevel + 1);
                        player.getPersistentData().putInt(StatsSystem.NBT_STAT_POINTS, currentPoints + 1);

                        StatsSystem.syncStats(player);
                    }
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}