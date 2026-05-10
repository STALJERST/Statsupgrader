package com.STALJER.statsupgrader.screan;

import com.STALJER.statsupgrader.menu.CustomInventoryMenu;
import com.STALJER.statsupgrader.network.ClientStatsData;
import com.STALJER.statsupgrader.network.LevelUpStatsPacket;
import com.STALJER.statsupgrader.network.ModMessages;
import com.STALJER.statsupgrader.network.UpgradeStatPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import com.STALJER.statsupgrader.StatsUpgrader;
import com.STALJER.statsupgrader.Config;

public class CustomInventoryScreen extends AbstractContainerScreen<CustomInventoryMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(StatsUpgrader.MODID, "textures/gui/stats_menu.png");
    private static final float TEXT_SCALE = 0.8f;

    public CustomInventoryScreen(CustomInventoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.statsupgrader.level_up"), (btn) -> {
            ModMessages.sendToServer(new LevelUpStatsPacket());
        }).bounds(x + 10, y + 54, 90, 14).build());

        // Маленькі кнопки "+" залишаються зсунутими вліво та піднятими
        this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> ModMessages.sendToServer(new UpgradeStatPacket("health"))).bounds(x + 135, y + 69, 16, 14).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> ModMessages.sendToServer(new UpgradeStatPacket("resistance"))).bounds(x + 135, y + 87, 16, 14).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> ModMessages.sendToServer(new UpgradeStatPacket("damage"))).bounds(x + 135, y + 105, 16, 14).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> ModMessages.sendToServer(new UpgradeStatPacket("speed"))).bounds(x + 135, y + 123, 16, 14).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> ModMessages.sendToServer(new UpgradeStatPacket("regen"))).bounds(x + 135, y + 141, 16, 14).build());
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Блокуємо стандартні написи
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(TEXT_SCALE, TEXT_SCALE, 1.0f);
        float invScale = 1.0f / TEXT_SCALE;

        int vanillaLevel = this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.experienceLevel : 0;

        drawScaledText(pGuiGraphics, Component.translatable("gui.statsupgrader.vanilla_level", vanillaLevel), x + 10, y + 5, 0x55FF55, invScale);
        drawScaledText(pGuiGraphics, Component.translatable("gui.statsupgrader.custom_level", ClientStatsData.statsLevel), x + 10, y + 18, 0x00FFFF, invScale);
        drawScaledText(pGuiGraphics, Component.translatable("gui.statsupgrader.free_points", ClientStatsData.statPoints), x + 10, y + 31, 0xFFFF55, invScale);


        int currentCost = com.STALJER.statsupgrader.StatsSystem.getLevelCost(ClientStatsData.statsLevel);
        Component costText = ClientStatsData.statsLevel >= Config.MAX_STATS_LEVEL.get() ? Component.literal("MAX") : Component.translatable("gui.statsupgrader.cost_info", currentCost);
        drawScaledText(pGuiGraphics, costText, x + 105, y + 57, 0xAAAAAA, invScale);

        String hpVal = String.format("%.1f", ClientStatsData.healthLevel * Config.HEALTH_INCREMENT.get() / 2.0);
        Component hpText = ClientStatsData.healthLevel >= Config.MAX_HEALTH_LEVEL.get() ? Component.translatable("gui.statsupgrader.health_max", hpVal) : Component.translatable("gui.statsupgrader.health", hpVal);
        drawScaledText(pGuiGraphics, hpText, x + 10, y + 72, 0xFFFFFF, invScale);

        String resVal = String.format("%.1f", ClientStatsData.resistanceLevel * Config.RESISTANCE_INCREMENT.get() * 100);
        Component resText = ClientStatsData.resistanceLevel >= Config.MAX_RESISTANCE_LEVEL.get() ? Component.translatable("gui.statsupgrader.resistance_max", resVal) : Component.translatable("gui.statsupgrader.resistance", resVal);
        drawScaledText(pGuiGraphics, resText, x + 10, y + 90, 0xFFFFFF, invScale);

        String dmgVal = String.format("%.1f", ClientStatsData.damageLevel * Config.DAMAGE_INCREMENT.get() * 100);
        Component dmgText = ClientStatsData.damageLevel >= Config.MAX_DAMAGE_LEVEL.get() ? Component.translatable("gui.statsupgrader.damage_max", dmgVal) : Component.translatable("gui.statsupgrader.damage", dmgVal);
        drawScaledText(pGuiGraphics, dmgText, x + 10, y + 108, 0xFFFFFF, invScale);

        String spdVal = String.format("%.1f", ClientStatsData.speedLevel * Config.SPEED_INCREMENT.get() * 100);
        Component spdText = ClientStatsData.speedLevel >= Config.MAX_SPEED_LEVEL.get() ? Component.translatable("gui.statsupgrader.speed_max", spdVal) : Component.translatable("gui.statsupgrader.speed", spdVal);
        drawScaledText(pGuiGraphics, spdText, x + 10, y + 126, 0xFFFFFF, invScale);

        String regVal = String.format("%.2f", ClientStatsData.regenLevel * Config.REGEN_INCREMENT.get());
        Component regText = ClientStatsData.regenLevel >= Config.MAX_REGEN_LEVEL.get() ? Component.translatable("gui.statsupgrader.regen_max", regVal) : Component.translatable("gui.statsupgrader.regen", regVal);
        drawScaledText(pGuiGraphics, regText, x + 10, y + 144, 0xFFFFFF, invScale);

        pGuiGraphics.pose().popPose();
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private void drawScaledText(GuiGraphics graphics, Component text, int x, int y, int color, float invScale) {
        graphics.drawString(this.font, text, (int)(x * invScale), (int)(y * invScale), color, false);
    }
}