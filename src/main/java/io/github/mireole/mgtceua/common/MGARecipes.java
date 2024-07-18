package io.github.mireole.mgtceua.common;

import appeng.api.AEApi;
import appeng.api.definitions.IParts;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.GTValues.L;
import static gregtech.api.unification.material.Materials.SolderingAlloy;

public class MGARecipes {
    public static void init() {
        registerAssemblerRecipes();
    }

    private static void registerAssemblerRecipes() {
        FluidStack solder = SolderingAlloy.getFluid(L / 2);

        IParts parts = AEApi.instance().definitions().parts();

        ItemStack machineController = MetaItems.COVER_MACHINE_CONTROLLER.getStackForm(1);
        ItemStack levelEmitter = parts.levelEmitter().maybeStack(1).orElse(ItemStack.EMPTY);
        ItemStack fluidLevelEmitter = parts.fluidLevelEmitter().maybeStack(1).orElse(ItemStack.EMPTY);

        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .input(machineController.getItem(), 1, machineController.getMetadata())
                .input(levelEmitter.getItem(), 1, levelEmitter.getMetadata())
                .input(fluidLevelEmitter.getItem(), 1, fluidLevelEmitter.getMetadata())
                .input(OrePrefix.circuit, MarkerMaterials.Tier.LV)
                .fluidInputs(solder)
                .outputs(MGAMetaItems.COVER_ITEM_MACHINE_CONTROLLER.getStackForm(1))
                .EUt(16).duration(100)
                .buildAndRegister();
    }
}
