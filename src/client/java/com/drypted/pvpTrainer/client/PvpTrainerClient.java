package com.drypted.pvpTrainer.client;

import com.drypted.pvpTrainer.client.config.ModConfig;
import com.drypted.pvpTrainer.client.renderer.PVPAttack;
import com.drypted.pvpTrainer.client.renderer.PVPLabels;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
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
    // cached screen size
    public static int ScreenW;
    public static int ScreenH;


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
            PVPLabels.init();
            PVPAttack.init();
        });

        // tick screens
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            // update screen size
            ScreenW = client.getWindow().getGuiScaledWidth();
            ScreenH = client.getWindow().getGuiScaledHeight();

            PVPLabels.tick(client);
            PVPAttack.tick(client);
        });

        // render screens
        HudElementRegistry.addLast(
                RENDER_LAYER, //
                (guiGraphics, deltaTracker) -> {
                    PVPLabels.render(guiGraphics, deltaTracker);
                    PVPAttack.render(guiGraphics, deltaTracker);
                }
        );
    }
}
