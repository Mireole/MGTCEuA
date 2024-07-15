package io.github.mireole.mgtceua.common;

import io.github.mireole.mgtceua.Tags;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class CommonProxy {

    public void preInit() {
        MGAMetaItems.init();
    }

}
