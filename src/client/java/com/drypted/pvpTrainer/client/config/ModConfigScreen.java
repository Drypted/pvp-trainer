package com.drypted.pvpTrainer.client.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


class ModConfigScreen
{
    public static Screen create(Screen parent)
    {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("com.drypted.pvptrainer.config.category.general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // General category
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable(
                "com.drypted.pvptrainer.config.category.general"));
        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("com.drypted.pvptrainer.config.option.enable_hud"),
                        config.enableHud
                )
                                 .setSaveConsumer(value -> config.enableHud = value)
                                 .setDefaultValue(ModConfig.DEFAULT.enableHud)
                                 .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("com.drypted.pvptrainer.config.option.show_in_creative"), config.showInCreative)
                                 .setSaveConsumer(value -> config.showInCreative = value)
                                 .setDefaultValue(ModConfig.DEFAULT.showInCreative)
                                 .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("com.drypted.pvptrainer.config.option.detect_mouse_buttons"), config.detectMouseButtons)
                                 .setSaveConsumer(value -> config.detectMouseButtons = value)
                                 .setDefaultValue(ModConfig.DEFAULT.detectMouseButtons)
                                 .build());

        // Pitch angle label category
        addLabelCategory(
                builder,
                entryBuilder,
                config.pitchAngleLabelConfig,
                "Pitch Angle",
                ModConfig.DEFAULT.pitchAngleLabelConfig
        );

        // Move state label category
        addLabelCategory(
                builder,
                entryBuilder,
                config.moveStateLabelConfig,
                "Move State",
                ModConfig.DEFAULT.moveStateLabelConfig
        );

        // Pressed key label category
        addLabelCategory(
                builder,
                entryBuilder,
                config.pressedKeyLabelConfig,
                "Pressed Key",
                ModConfig.DEFAULT.pressedKeyLabelConfig
        );

        // Hotbar category
        ConfigCategory hotbar = builder.getOrCreateCategory(Component.translatable("com.drypted.pvptrainer.config.category.hotbar"));
        hotbar.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable(
                                "com.drypted.pvptrainer.config.option.hotbar.show_keybinds"),
                        config.hotbar.showHotbarKeybinds
                )
                                .setSaveConsumer(value -> config.hotbar.showHotbarKeybinds = value)
                                .setDefaultValue(ModConfig.DEFAULT.hotbar.showHotbarKeybinds)
                                .build());

        hotbar.addEntry(entryBuilder.startColorField(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.text_color"), config.hotbar.textColor)
                                .setSaveConsumer(value -> config.hotbar.textColor = value)
                                .setDefaultValue(ModConfig.DEFAULT.hotbar.textColor)
                                .build());

        hotbar.addEntry(entryBuilder.startColorField(
                        Component.translatable(
                                "com.drypted.pvptrainer.config.option.label.background_color"),
                        config.hotbar.backgroundColor
                )
                                .setSaveConsumer(value -> config.hotbar.backgroundColor = value)
                                .setDefaultValue(ModConfig.DEFAULT.hotbar.backgroundColor)
                                .build());

        hotbar.addEntry(entryBuilder.startIntField(
                        Component.translatable(
                                "com.drypted.pvptrainer.config.option.label.background_opacity"),
                        config.hotbar.backgroundColorOpacity
                )
                                .setSaveConsumer(value -> config.hotbar.backgroundColorOpacity = value)
                                .setDefaultValue(ModConfig.DEFAULT.hotbar.backgroundColorOpacity)
                                .build());

        // Save handler - writes config to disk when the "save" button is pressed in the cloth-config screen
        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(ModConfig.class).save());

        return builder.build();
    }

    public static void addLabelCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig.LabelConfig labelConfig, String labelName, ModConfig.LabelConfig defaultConfig)
    {
        // Title
        ConfigCategory category = builder.getOrCreateCategory(Component.literal(labelName + " Label"));

        // Enabled
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.enabled"), labelConfig.enabled)
                                  .setSaveConsumer(value -> labelConfig.enabled = value)
                                  .setDefaultValue(defaultConfig.enabled)
                                  .build());
        // Position
        category.addEntry(entryBuilder.startEnumSelector(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.position"),
                        ModConfig.LabelPosition.class,
                        labelConfig.position
                )  //
                                  .setDefaultValue(labelConfig.position)
                                  .setSaveConsumer(value -> labelConfig.position = value)
                                  .build());
        // Text color
        category.addEntry(entryBuilder.startColorField(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.text_color"), labelConfig.textColor)
                                  .setSaveConsumer(value -> labelConfig.textColor = value)
                                  .setDefaultValue(defaultConfig.textColor)
                                  .build());
        // Background color
        category.addEntry(entryBuilder.startColorField(
                Component.translatable(
                        "com.drypted.pvptrainer.config.option.label.background_color"),
                labelConfig.backgroundColor
        ).setDefaultValue(defaultConfig.backgroundColor).setSaveConsumer(value -> labelConfig.backgroundColor = value).build());
        // Background opacity
        category.addEntry(entryBuilder.startIntField(
                        Component.translatable(
                                "com.drypted.pvptrainer.config.option.label.background_opacity"),
                        labelConfig.backgroundColorOpacity
                )
                                  .setSaveConsumer(value -> labelConfig.backgroundColorOpacity = value)
                                  .setDefaultValue(defaultConfig.backgroundColorOpacity)
                                  .build());
        // Padding
        category.addEntry(entryBuilder.startIntField(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.padding"),
                        labelConfig.padding
                )
                                  .setSaveConsumer(value -> labelConfig.padding = value)
                                  .setDefaultValue(defaultConfig.padding)
                                  .build());
        // Margin
        category.addEntry(entryBuilder.startIntField(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.margin"),
                        labelConfig.margin
                )
                                  .setSaveConsumer(value -> labelConfig.margin = value)
                                  .setDefaultValue(defaultConfig.margin)
                                  .build());
        // Label gap
        category.addEntry(entryBuilder.startIntField(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.stack_gap"),
                        labelConfig.stackGap
                )
                                  .setSaveConsumer(value -> labelConfig.stackGap = value)
                                  .setDefaultValue(defaultConfig.stackGap)
                                  .build());
    }
}
