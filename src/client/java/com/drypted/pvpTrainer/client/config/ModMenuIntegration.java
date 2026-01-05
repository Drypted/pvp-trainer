package com.drypted.pvpTrainer.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory()
    {
        return ModConfigScreen::create;
    }
}