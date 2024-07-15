package io.github.mireole.mgtceua.common.items;

import gregtech.api.items.metaitem.MetaItem;

import java.util.List;

public class MGAMetaItems {

    public static MetaItem<?>.MetaValueItem COVER_ITEM_MACHINE_CONTROLLER;

    public static void init() {
        MGAMetaItem item = new MGAMetaItem();
        item.setRegistryName("mga_meta_item");
    }

}
