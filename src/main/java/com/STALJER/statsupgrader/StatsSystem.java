package com.STALJER.statsupgrader;

import com.STALJER.statsupgrader.network.ModMessages;
import com.STALJER.statsupgrader.network.SyncStatsPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = StatsUpgrader.MODID)
public class StatsSystem {
    public static final String NBT_STATS_LEVEL = "su_stats_level";
    public static final String NBT_STAT_POINTS = "su_stat_points";
    public static final String NBT_HEALTH = "su_hp_level";
    public static final String NBT_RESISTANCE = "su_res_level";
    public static final String NBT_DAMAGE = "su_dmg_level";
    public static final String NBT_SPEED = "su_spd_level";
    public static final String NBT_REGEN = "su_reg_level";


    public static final UUID HEALTH_MOD_UUID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    public static final UUID DAMAGE_MOD_UUID = UUID.fromString("66666666-7777-8888-9999-000000000000");
    public static final UUID SPEED_MOD_UUID = UUID.fromString("77777777-8888-9999-0000-111111111111");

    public static int getLevelCost(int currentLevel) {
        return (int) Math.round(5 + Math.pow(0.5, currentLevel - 1));
    }

    public static void syncStats(ServerPlayer player) {
        CompoundTag nbt = player.getPersistentData();
        ModMessages.sendToPlayer(new SyncStatsPacket(
                nbt.contains(NBT_STATS_LEVEL) ? nbt.getInt(NBT_STATS_LEVEL) : 1,
                nbt.getInt(NBT_STAT_POINTS), nbt.getInt(NBT_HEALTH),
                nbt.getInt(NBT_RESISTANCE), nbt.getInt(NBT_DAMAGE),
                nbt.getInt(NBT_SPEED), nbt.getInt(NBT_REGEN)
        ), player);
    }


    public static void reapplyAttributes(ServerPlayer player) {
        CompoundTag nbt = player.getPersistentData();

        AttributeInstance hpAttr = player.getAttribute(Attributes.MAX_HEALTH);
        if (hpAttr != null) {
            hpAttr.removeModifier(HEALTH_MOD_UUID);
            int hpLvl = nbt.getInt(NBT_HEALTH);
            if (hpLvl > 0) {
                hpAttr.addPermanentModifier(new AttributeModifier(HEALTH_MOD_UUID, "Health Upgrade", hpLvl * Config.HEALTH_INCREMENT.get(), AttributeModifier.Operation.ADDITION));
            }
        }


        AttributeInstance dmgAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (dmgAttr != null) {
            dmgAttr.removeModifier(DAMAGE_MOD_UUID);
            int dmgLvl = nbt.getInt(NBT_DAMAGE);
            if (dmgLvl > 0) {
                dmgAttr.addPermanentModifier(new AttributeModifier(DAMAGE_MOD_UUID, "Damage Upgrade", dmgLvl * Config.DAMAGE_INCREMENT.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }


        AttributeInstance spdAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (spdAttr != null) {
            spdAttr.removeModifier(SPEED_MOD_UUID);
            int spdLvl = nbt.getInt(NBT_SPEED);
            if (spdLvl > 0) {
                spdAttr.addPermanentModifier(new AttributeModifier(SPEED_MOD_UUID, "Speed Upgrade", spdLvl * Config.SPEED_INCREMENT.get(), AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            reapplyAttributes(player); // Застосовуємо при вході на сервер
            syncStats(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getEntity() instanceof ServerPlayer newPlayer) {
            CompoundTag oldNbt = event.getOriginal().getPersistentData();
            CompoundTag newNbt = newPlayer.getPersistentData();

            // Копіюємо пам'ять
            newNbt.putInt(NBT_STATS_LEVEL, oldNbt.contains(NBT_STATS_LEVEL) ? oldNbt.getInt(NBT_STATS_LEVEL) : 1);
            newNbt.putInt(NBT_STAT_POINTS, oldNbt.getInt(NBT_STAT_POINTS));
            newNbt.putInt(NBT_HEALTH, oldNbt.getInt(NBT_HEALTH));
            newNbt.putInt(NBT_RESISTANCE, oldNbt.getInt(NBT_RESISTANCE));
            newNbt.putInt(NBT_DAMAGE, oldNbt.getInt(NBT_DAMAGE));
            newNbt.putInt(NBT_SPEED, oldNbt.getInt(NBT_SPEED));
            newNbt.putInt(NBT_REGEN, oldNbt.getInt(NBT_REGEN));


            reapplyAttributes(newPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            int resLevel = player.getPersistentData().getInt(NBT_RESISTANCE);
            if (resLevel > 0) {
                float reduction = resLevel * Config.RESISTANCE_INCREMENT.get().floatValue();
                if (reduction > 0.8f) reduction = 0.8f;
                event.setAmount(event.getAmount() * (1.0f - reduction));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            if (event.player.tickCount % 20 == 0) {
                int regenLevel = event.player.getPersistentData().getInt(NBT_REGEN);
                if (regenLevel > 0 && event.player.getHealth() < event.player.getMaxHealth()) {
                    event.player.heal(regenLevel * Config.REGEN_INCREMENT.get().floatValue());
                }
            }
        }
    }
}