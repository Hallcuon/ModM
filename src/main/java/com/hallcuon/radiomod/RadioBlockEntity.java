package com.hallcuon.radiomod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;

public class RadioBlockEntity extends BlockEntity {
    private boolean isPlaying = false;

    public RadioBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RADIO.get(), pos, state);
    }

    public void togglePlayback() {
        if (isPlaying) {
            stop();
        } else {
            start();
        }
    }

    public void start() {
        isPlaying = true;
        AudioManager.getInstance().play("https://www.youtube.com/watch?v=35LrV-V-ORY");
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        isPlaying = false;
        AudioManager.getInstance().stop();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RadioBlockEntity entity) {
        // Логіка оновлення (за потреби)
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("IsPlaying", isPlaying);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        isPlaying = tag.getBoolean("IsPlaying");
    }
}
