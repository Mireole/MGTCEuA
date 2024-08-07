package com.enderio.core.common.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public interface IOverlayRenderAware {

    void renderItemOverlayIntoGUI(@Nonnull ItemStack stack, int xPosition, int yPosition);

}
