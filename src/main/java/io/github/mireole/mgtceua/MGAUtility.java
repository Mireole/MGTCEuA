package io.github.mireole.mgtceua;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class MGAUtility {
    @NotNull
    public static ResourceLocation mgaId(@NotNull String path) {
        return new ResourceLocation(Tags.MODID, path);
    }
}
