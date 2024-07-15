package io.github.mireole.mgtceua.common;

import gregtech.api.GregTechAPI;
import gregtech.api.cover.CoverDefinition;
import io.github.mireole.mgtceua.Tags;
import io.github.mireole.mgtceua.common.covers.MGACovers;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Tags.MODID)
public class CommonProxy {

    public void preInit() {
        MGAMetaItems.init();
    }

    @SubscribeEvent
    public static void registerCovers(GregTechAPI.RegisterEvent<CoverDefinition> event) {
        MGACovers.init();
    }

}
