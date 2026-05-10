package com.STALJER.statsupgrader.network;

import com.STALJER.statsupgrader.StatsSystem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpgradeStatPacket {

    private final String statToUpgrade;

    public UpgradeStatPacket(String statToUpgrade) { this.statToUpgrade = statToUpgrade; }
    public UpgradeStatPacket(FriendlyByteBuf buf) { this.statToUpgrade = buf.readUtf(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeUtf(this.statToUpgrade); }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                int currentPoints = player.getPersistentData().getInt(StatsSystem.NBT_STAT_POINTS);

                if (currentPoints >= 1) {
                    boolean success = false;
                    CompoundTag nbt = player.getPersistentData();

                    if (this.statToUpgrade.equals("health")) {
                        int lvl = nbt.getInt(StatsSystem.NBT_HEALTH);
                        if (lvl < com.STALJER.statsupgrader.Config.MAX_HEALTH_LEVEL.get()) {
                            nbt.putInt(StatsSystem.NBT_HEALTH, lvl + 1);
                            player.heal(com.STALJER.statsupgrader.Config.HEALTH_INCREMENT.get().floatValue());
                            success = true;
                        }
                    } else if (this.statToUpgrade.equals("resistance")) {
                        int lvl = nbt.getInt(StatsSystem.NBT_RESISTANCE);
                        if (lvl < com.STALJER.statsupgrader.Config.MAX_RESISTANCE_LEVEL.get()) {
                            nbt.putInt(StatsSystem.NBT_RESISTANCE, lvl + 1);
                            success = true;
                        }
                    } else if (this.statToUpgrade.equals("damage")) {
                        int lvl = nbt.getInt(StatsSystem.NBT_DAMAGE);
                        if (lvl < com.STALJER.statsupgrader.Config.MAX_DAMAGE_LEVEL.get()) {
                            nbt.putInt(StatsSystem.NBT_DAMAGE, lvl + 1);
                            success = true;
                        }
                    } else if (this.statToUpgrade.equals("speed")) {
                        int lvl = nbt.getInt(StatsSystem.NBT_SPEED);
                        if (lvl < com.STALJER.statsupgrader.Config.MAX_SPEED_LEVEL.get()) {
                            nbt.putInt(StatsSystem.NBT_SPEED, lvl + 1);
                            success = true;
                        }
                    } else if (this.statToUpgrade.equals("regen")) {
                        int lvl = nbt.getInt(StatsSystem.NBT_REGEN);
                        if (lvl < com.STALJER.statsupgrader.Config.MAX_REGEN_LEVEL.get()) {
                            nbt.putInt(StatsSystem.NBT_REGEN, lvl + 1);
                            success = true;
                        }
                    }

                    if (success) {
                        nbt.putInt(StatsSystem.NBT_STAT_POINTS, currentPoints - 1);
                        // Застосовуємо атрибути і оновлюємо екран
                        StatsSystem.reapplyAttributes(player);
                        StatsSystem.syncStats(player);
                    }
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}