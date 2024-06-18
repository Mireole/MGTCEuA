package io.github.mireole.mgtceua.common.items;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.common.items.MetaItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MGAMetaItems {
    private static final List<MetaItem<?>> ITEMS = MetaItem.getMetaItems();

    public static MetaItem<?>.MetaValueItem COVER_ITEM_MACHINE_CONTROLLER;
    public static MetaItem<?>.MetaValueItem COVER_FLUID_MACHINE_CONTROLLER;

    public static void init() {
        MGAMetaItem item = new MGAMetaItem();
        item.setRegistryName("mga_meta_item");
    }

}
