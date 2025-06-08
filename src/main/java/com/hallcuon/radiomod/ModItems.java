package com.hallcuon.radiomod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);

    public static final RegistryObject<Item> RADIO_BLOCK_ITEM = ITEMS.register("radio_block",
            () -> new BlockItem(ModBlocks.RADIO_BLOCK.get(),
                    new Item.Properties()));
}