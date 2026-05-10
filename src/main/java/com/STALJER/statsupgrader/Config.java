package com.STALJER.statsupgrader;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.IntValue MAX_STATS_LEVEL;
    public static final ForgeConfigSpec.IntValue MAX_HEALTH_LEVEL;
    public static final ForgeConfigSpec.IntValue MAX_RESISTANCE_LEVEL;
    public static final ForgeConfigSpec.IntValue MAX_DAMAGE_LEVEL;
    public static final ForgeConfigSpec.IntValue MAX_SPEED_LEVEL;
    public static final ForgeConfigSpec.IntValue MAX_REGEN_LEVEL;


    public static final ForgeConfigSpec.DoubleValue HEALTH_INCREMENT;
    public static final ForgeConfigSpec.DoubleValue RESISTANCE_INCREMENT;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_INCREMENT;
    public static final ForgeConfigSpec.DoubleValue SPEED_INCREMENT;
    public static final ForgeConfigSpec.DoubleValue REGEN_INCREMENT;

    static {
        BUILDER.push("Max Levels");
        MAX_STATS_LEVEL = BUILDER.defineInRange("maxStatsLevel", 65, 1, 1000);
        MAX_HEALTH_LEVEL = BUILDER.defineInRange("maxHealthLevel", 20, 1, 100);
        MAX_RESISTANCE_LEVEL = BUILDER.defineInRange("maxResistanceLevel", 20, 1, 100);
        MAX_DAMAGE_LEVEL = BUILDER.defineInRange("maxDamageLevel", 20, 1, 100);
        MAX_SPEED_LEVEL = BUILDER.defineInRange("maxSpeedLevel", 20, 1, 100);
        MAX_REGEN_LEVEL = BUILDER.defineInRange("maxRegenLevel", 10, 1, 100);
        BUILDER.pop();

        BUILDER.push("Increments");
        HEALTH_INCREMENT = BUILDER.defineInRange("healthIncrement", 2.0, 0.5, 20.0);
        RESISTANCE_INCREMENT = BUILDER.defineInRange("resistanceIncrement", 0.005, 0.001, 1.0);
        DAMAGE_INCREMENT = BUILDER.defineInRange("damageIncrement", 0.005, 0.001, 1.0);
        SPEED_INCREMENT = BUILDER.defineInRange("speedIncrement", 0.005, 0.001, 1.0);
        REGEN_INCREMENT = BUILDER.defineInRange("regenIncrement", 0.05, 0.01, 10.0);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}