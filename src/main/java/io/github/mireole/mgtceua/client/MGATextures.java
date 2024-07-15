package io.github.mireole.mgtceua.client;

import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MGATextures {
    public static SimpleOverlayRenderer ME_MACHINE_CONTROLLER;

    public static void preInit() {
        ME_MACHINE_CONTROLLER = new SimpleOverlayRenderer("mgtceua:cover/overlay_me_controller");
    }

}
