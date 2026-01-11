package com.drypted.pvpTrainer.client;

import com.drypted.pvpTrainer.client.config.ModConfig;
import com.drypted.pvpTrainer.client.hudOverlay.PVPAttackDetector;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
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
    // config
    public static ModConfig CONFIG;
    public static Minecraft CLIENT;

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
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            CLIENT = client;
            PVPLabels.init();
            PVPAttackDetector.init();
        });

        // tick screens
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            PVPLabels.tick(client);
            PVPAttackDetector.tick(client);
        });

        // render screens
        HudElementRegistry.addLast(
                RENDER_LAYER, //
                (guiGraphics, deltaTracker) -> {
                    PVPLabels.render(guiGraphics, deltaTracker);
                    PVPAttackDetector.render(guiGraphics, deltaTracker);
                }
        );
    }
}
