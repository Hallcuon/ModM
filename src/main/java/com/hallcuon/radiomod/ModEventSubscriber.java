package com.hallcuon.radiomod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber(modid = "radiomod", value = Dist.CLIENT)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                Level world = player.level();
                BlockPos playerPos = player.blockPosition();

                // Перевірка у радіусі 5 блоків
                int radius = 5;

                for (BlockPos pos : BlockPos.betweenClosed(playerPos.offset(-radius, -radius, -radius),
                        playerPos.offset(radius, radius, radius))) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity instanceof RadioBlockEntity radio) {
                        if (!radio.isPlaying()) {
                            radio.start();
                        }
                    }
                }
            }
        }
    }
}
