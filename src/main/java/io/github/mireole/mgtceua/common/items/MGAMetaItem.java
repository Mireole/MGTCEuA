package io.github.mireole.mgtceua.common.items;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import gregtech.api.util.GTUtility;
import io.github.mireole.mgtceua.MGAUtility;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static io.github.mireole.mgtceua.MGAUtility.mgaId;
import static io.github.mireole.mgtceua.common.items.MGAMetaItems.*;

public class MGAMetaItem extends StandardMetaItem {

    public MGAMetaItem() {
        super();
    }

    @Override
    public ResourceLocation createItemModelPath(MetaItem<?>.MetaValueItem metaValueItem, String postfix) {
        return MGAUtility.mgaId(this.formatModelPath(metaValueItem) + postfix);
    }

    @Override
    public void registerSubItems() {
        // ME machine controller covers 0-1
        COVER_ITEM_MACHINE_CONTROLLER = addItem(0, "cover.me_item_machine_controller");
        COVER_FLUID_MACHINE_CONTROLLER = addItem(1, "cover.me_fluid_machine_controller");
        // TODO: advanced me machine controller (multiple conditions, fluids & items)
    }
}
