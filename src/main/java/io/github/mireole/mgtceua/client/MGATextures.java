package io.github.mireole.mgtceua.client;

import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MGATextures {
    public static SimpleOverlayRenderer ME_MACHINE_CONTROLLER_ON;
    public static SimpleOverlayRenderer ME_MACHINE_CONTROLLER_OFF;

    public static void preInit() {
        ME_MACHINE_CONTROLLER_ON = new SimpleOverlayRenderer("mgtceua:cover/overlay_me_controller_on");
        ME_MACHINE_CONTROLLER_OFF = new SimpleOverlayRenderer("mgtceua:cover/overlay_me_controller_off");
    }

}
