package com.hallcuon.radiomod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RadioScreen extends Screen {
    private EditBox urlField;
    private final AudioManager audioManager = AudioManager.getInstance();
    private boolean isPlaying = false;

    public RadioScreen() {
        super(Component.literal("♫ Радіо Мод"));
    }

    @Override
    protected void init() {
        int fieldWidth = 300;
        this.urlField = new EditBox(
                this.font,
                this.width / 2 - fieldWidth / 2,
                60,
                fieldWidth,
                20,
                Component.literal("Введіть URL YouTube")
        );
        this.urlField.setMaxLength(512);
        this.addRenderableWidget(urlField);

        this.addRenderableWidget(Button.builder(
                        Component.literal("▶ Відтворити"),
                        button -> {
                            if (!isPlaying) {
                                playAudio();
                            }
                        })
                .pos(this.width / 2 - 100, 100)
                .size(90, 20)
                .build()
        );

        this.addRenderableWidget(Button.builder(
                        Component.literal("⏹ Стоп"),
                        button -> stopAudio())
                .pos(this.width / 2 + 10, 100)
                .size(90, 20)
                .build()
        );
    }

    private void playAudio() {
        String url = urlField.getValue().trim();
        if (url.isEmpty()) {
            Minecraft.getInstance().player.displayClientMessage(
                    Component.literal("❗ Будь ласка, введіть URL"),
                    false
            );
            return;
        }

        isPlaying = true;
        new Thread(() -> {
            try {
                audioManager.play(url);
                Minecraft.getInstance().player.displayClientMessage(
                        Component.literal("♫ Зараз грає: " + url),
                        false
                );
            } catch (Exception e) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.literal("❗ Помилка: " + e.getMessage()),
                        false
                );
                isPlaying = false;
            }
        }).start();
    }

    private void stopAudio() {
        audioManager.stop();
        isPlaying = false;
        Minecraft.getInstance().player.displayClientMessage(
                Component.literal("⏹ Відтворення зупинено"),
                false
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(
                this.font,
                "♫ Радіо Мод",
                this.width / 2,
                30,
                0x55FF55
        );

        if (urlField.getValue().isEmpty()) {
            guiGraphics.drawString(
                    this.font,
                    "Приклад: https://youtu.be/dQw4w9WgXcQ",
                    this.width / 2 - 100,
                    85,
                    0xAAAAAA
            );
        }

        if (isPlaying) {
            guiGraphics.drawString(
                    this.font,
                    "Статус: грає...",
                    this.width / 2 - 100,
                    120,
                    0x55FF55
            );
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
