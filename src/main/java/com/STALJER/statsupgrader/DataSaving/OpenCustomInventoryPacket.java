package com.STALJER.statsupgrader.DataSaving;

import com.STALJER.statsupgrader.menu.CustomInventoryMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class OpenCustomInventoryPacket {
    public OpenCustomInventoryPacket() {}
    public OpenCustomInventoryPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.openMenu(new SimpleMenuProvider(
                        (id, playerInv, p) -> new CustomInventoryMenu(id, playerInv),
                        Component.translatable("gui.statsupgrader.custom_inventory")
                ));
            }
        });

        // ВАЖЛИВО: Кажемо грі, що пакет оброблено успішно
        context.setPacketHandled(true);
        return true;
    }
}