package io.github.mireole.mgtceua.common;

import gregtech.api.GregTechAPI;
import gregtech.api.cover.CoverDefinition;
import io.github.mireole.mgtceua.Tags;
import io.github.mireole.mgtceua.common.covers.MGACovers;
import io.github.mireole.mgtceua.common.items.MGAMetaItem;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class CommonProxy {

    public void preInit() {
        MGAMetaItems.init();
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void registerCovers(GregTechAPI.RegisterEvent<CoverDefinition> event) {
        MGACovers.init();
    }

}
