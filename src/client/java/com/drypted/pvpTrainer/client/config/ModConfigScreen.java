package com.drypted.pvpTrainer.client.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;


class ModConfigScreen extends Screen
{
    private final Screen parent;
    private Screen outputScreen;

    public ModConfigScreen(Screen parent)
    {
        super(Component.translatable("com.drypted.pvptrainer.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("com.drypted.pvptrainer.config.category.general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // General category
        addGeneralCategory(builder, entryBuilder, config);

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
        addHotbarCategory(builder, entryBuilder, config);

        // Reset All Button
        addResetAllButton();

        // Save handler - writes config to disk when the "save" button is pressed in the cloth-config screen
        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(ModConfig.class).save());

        // build output screen
        this.outputScreen = builder.build();
        this.outputScreen.init(Minecraft.getInstance(), this.width, this.height);
    }

    private void addGeneralCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig config)
    {
        builder.setTitle(Component.translatable("com.drypted.pvptrainer.config.category.general"));
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
    }

    private void addHotbarCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig config)
    {
        builder.setTitle(Component.translatable("com.drypted.pvptrainer.config.category.hotbar"));

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
    }

    public void addLabelCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig.LabelConfig labelConfig, String labelName, ModConfig.LabelConfig defaultConfig)
    {
        builder.setTitle(Component.literal(labelName + " Label"));

        // Title
        ConfigCategory category = builder.getOrCreateCategory(Component.literal(labelName + " Label"));

        // Enabled
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.enabled"), labelConfig.enabled)
                                  .setSaveConsumer(value -> labelConfig.enabled = value)
                                  .setDefaultValue(defaultConfig.enabled)
                                  .build());
        // Position
        ArrayList<String> positionSelections = new ArrayList<>();
        for (ModConfig.LabelPosition pos: ModConfig.LabelPosition.values())
        {
            positionSelections.add(pos.name());
        }
        category.addEntry(entryBuilder.startStringDropdownMenu(
                        Component.translatable(
                                "com.drypted.pvptrainer.config.option.label.position"), labelConfig.position.name()
                )
                                  .setDefaultValue(labelConfig.position.name())
                                  .setSuggestionMode(false)
                                  .setSelections(positionSelections)
                                  .setSaveConsumer(value -> {
                                      labelConfig.position = ModConfig.LabelPosition.valueOf(value);
                                  })
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

        // Creative dropdown
        SubCategoryBuilder subCat = entryBuilder.startSubCategory(Component.translatable(
                "com.drypted.pvptrainer.config.option.label.creative_settings")).setExpanded(false);

        // Creative margin
        subCat.add(entryBuilder.startIntField(
                        Component.translatable("com.drypted.pvptrainer.config.option.label.creative_margin"),
                        labelConfig.creativeMargin
                )
                           .setSaveConsumer(value -> labelConfig.creativeMargin = value)
                           .setDefaultValue(defaultConfig.creativeMargin)
                           .build());

        category.addEntry(subCat.build());
    }

    private void addResetAllButton()
    {
        this.addRenderableWidget(Button.builder(
                Component.translatable("com.drypted.pvptrainer.config.button.reset_all"), button -> {
                    // reset config
                    AutoConfig.getConfigHolder(ModConfig.class).setConfig(ModConfig.DEFAULT);
                    AutoConfig.getConfigHolder(ModConfig.class).save();
                    // refresh
                    Minecraft.getInstance().setScreen(new ModConfigScreen(parent));
                }
        ).bounds(this.width - 90, 6, 80, 20).build());
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta)
    {
        outputScreen.render(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl)
    {
        return outputScreen.mouseClicked(mouseButtonEvent, bl) || super.mouseClicked(mouseButtonEvent, bl);
    }


    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent)
    {
        return outputScreen.mouseReleased(mouseButtonEvent) || super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g)
    {
        return outputScreen.mouseScrolled(d, e, f, g) || super.mouseScrolled(d, e, f, g);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent)
    {
        return outputScreen.keyPressed(keyEvent) || super.keyPressed(keyEvent);
    }

    @Override
    public void onClose()
    {
        Minecraft.getInstance().setScreen(parent);
    }
}
