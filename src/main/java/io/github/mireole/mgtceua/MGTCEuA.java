package io.github.mireole.mgtceua;

import gregtech.api.GregTechAPI;
import gregtech.api.cover.CoverDefinition;
import io.github.mireole.mgtceua.common.CommonProxy;
import io.github.mireole.mgtceua.common.covers.MGACovers;
import io.github.mireole.mgtceua.common.items.MGAMetaItem;
import io.github.mireole.mgtceua.common.items.MGAMetaItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.12.2]")
public class MGTCEuA {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);
    @SidedProxy(modId = Tags.MODID, clientSide = "io.github.mireole.mgtceua.client.ClientProxy", serverSide = "io.github.mireole.mgtceua.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit();
    }

}
