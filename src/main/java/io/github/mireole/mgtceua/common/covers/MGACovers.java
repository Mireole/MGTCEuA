package io.github.mireole.mgtceua.common.covers;

import gregtech.common.covers.CoverBehaviors;
import io.github.mireole.mgtceua.common.items.MGAMetaItem;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;

import static io.github.mireole.mgtceua.MGAUtility.mgaId;

public final class MGACovers {
    public static void init() {
        CoverBehaviors.registerBehavior(mgaId("item_me_machine_controller"), MGAMetaItems.COVER_ITEM_MACHINE_CONTROLLER, MEItemMachineController::new);
        CoverBehaviors.registerBehavior(mgaId("fluid_me_machine_controller"), MGAMetaItems.COVER_FLUID_MACHINE_CONTROLLER, MEFluidMachineController::new);
    }
}
