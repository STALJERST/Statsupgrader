package com.STALJER.statsupgrader.network;

import com.STALJER.statsupgrader.DataSaving.OpenCustomInventoryPacket;
import com.STALJER.statsupgrader.StatsUpgrader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(StatsUpgrader.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        net.messageBuilder(OpenCustomInventoryPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(OpenCustomInventoryPacket::new)
                .encoder(OpenCustomInventoryPacket::toBytes)
                .consumerMainThread(OpenCustomInventoryPacket::handle)
                .add();


        net.messageBuilder(UpgradeStatPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpgradeStatPacket::new)
                .encoder(UpgradeStatPacket::toBytes)
                .consumerMainThread(UpgradeStatPacket::handle)
                .add();


        net.messageBuilder(LevelUpStatsPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LevelUpStatsPacket::new)
                .encoder(LevelUpStatsPacket::toBytes)
                .consumerMainThread(LevelUpStatsPacket::handle)
                .add();


        net.messageBuilder(SyncStatsPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncStatsPacket::new)
                .encoder(SyncStatsPacket::toBytes)
                .consumerMainThread(SyncStatsPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}