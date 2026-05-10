package com.STALJER.statsupgrader.Buttons;

import com.STALJER.statsupgrader.DataSaving.OpenCustomInventoryPacket;
import com.STALJER.statsupgrader.network.ModMessages;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.STALJER.statsupgrader.StatsUpgrader.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class InventoryButtonHandler {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            int guiLeft = screen.getGuiLeft();
            int guiTop = screen.getGuiTop();


            Button customTabButton = Button.builder(Component.empty(), (btn) -> {
                        ModMessages.sendToServer(new OpenCustomInventoryPacket());
                    })
                    .bounds(guiLeft + 130, guiTop + 60, 20, 20) // Я зробив її 20x20, як звичайну кнопку з іконкою
                    .build();

            event.addListener(customTabButton);
        }
    }
}