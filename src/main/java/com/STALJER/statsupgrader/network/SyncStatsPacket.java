package com.STALJER.statsupgrader.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncStatsPacket {
    private final int level, points, health, resistance, damage, speed, regen;

    public SyncStatsPacket(int level, int points, int health, int resistance, int damage, int speed, int regen) {
        this.level = level; this.points = points; this.health = health; this.resistance = resistance;
        this.damage = damage; this.speed = speed; this.regen = regen;
    }

    public SyncStatsPacket(FriendlyByteBuf buf) {
        this.level = buf.readInt(); this.points = buf.readInt(); this.health = buf.readInt();
        this.resistance = buf.readInt(); this.damage = buf.readInt(); this.speed = buf.readInt(); this.regen = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(level); buf.writeInt(points); buf.writeInt(health); buf.writeInt(resistance);
        buf.writeInt(damage); buf.writeInt(speed); buf.writeInt(regen);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientStatsData.statsLevel = this.level;
            ClientStatsData.statPoints = this.points;
            ClientStatsData.healthLevel = this.health;
            ClientStatsData.resistanceLevel = this.resistance;
            ClientStatsData.damageLevel = this.damage;
            ClientStatsData.speedLevel = this.speed;
            ClientStatsData.regenLevel = this.regen;
        });
        context.setPacketHandled(true);
        return true;
    }
}