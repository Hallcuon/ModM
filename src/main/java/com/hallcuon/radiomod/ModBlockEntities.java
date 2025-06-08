package com.hallcuon.radiomod;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "radiomod");

    public static final RegistryObject<BlockEntityType<RadioBlockEntity>> RADIO =
            BLOCK_ENTITIES.register("radio",
                    () -> BlockEntityType.Builder.of(RadioBlockEntity::new, ModBlocks.RADIO_BLOCK.get())
                            .build(null));
}
