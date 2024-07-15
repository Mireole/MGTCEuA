package io.github.mireole.mgtceua.common.covers;

import gregtech.common.covers.CoverBehaviors;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;

import static io.github.mireole.mgtceua.MGAUtility.mgaId;

public final class MGACovers {
    public static void init() {
        CoverBehaviors.registerBehavior(mgaId("me_machine_controller"), MGAMetaItems.COVER_ITEM_MACHINE_CONTROLLER, MEMachineController::new);
    }
}
