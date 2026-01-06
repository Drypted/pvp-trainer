package com.drypted.pvpTrainer.client;

import com.drypted.pvpTrainer.client.config.ModConfig;
import com.drypted.pvpTrainer.client.utils.PVPScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PvpTrainerClient implements ClientModInitializer
{
    public static final String MOD_ID = "pvp-trainer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // render layer
    public static final ResourceLocation RENDER_LAYER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "pvp-trainer-layer");
    public static int CURRENT_SLOT_CLIENT = 0;
    // config
    public static ModConfig CONFIG;

    private static void renderHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        PVPScreen.render(guiGraphics, deltaTracker);
    }

    @Override
    public void onInitializeClient()
    {
        // init config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((holder, newConfig) -> {
            CONFIG = newConfig;
            return InteractionResult.PASS;
        });

        // init screens
        PVPScreen.init();

        // add layers
        HudElementRegistry.addLast(RENDER_LAYER, PvpTrainerClient::renderHud);
    }
}
