package eu.thog92.isbrh;

import eu.thog92.isbrh.example.BlockExample;
import eu.thog92.isbrh.example.RenderExample;
import eu.thog92.isbrh.registry.RenderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = "isbrhcore", name = "ISBRH Core", acceptedMinecraftVersions = "[1.8]")
public class ISBRH {
    public static final Block test = new BlockExample().setUnlocalizedName("test");
    public static final ResourceLocation textureLocation = new ResourceLocation(
            "isbrhcore:blocks/test");
    public static int testId;
    private static boolean exempleEnabled = Boolean.parseBoolean(System.getProperty("isbrhCore.enableExemple", "false"));
    private Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        if(FMLCommonHandler.instance().getSide().isServer())
        {
            logger.error("WHY AM I HERE? I'm a client side only mod!");
        }
        else if (exempleEnabled) {
            logger.info("Registering ISBRH Block Exemple...");
            testId = RenderRegistry.getNextAvailableRenderId();
            GameRegistry.registerBlock(test, "test");
            RenderRegistry.registerBlockHandler(new RenderExample());
        }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.map == Minecraft.getMinecraft().getTextureMapBlocks()) {
            RenderRegistry.instance().injectTexture(event.map);
        }
    }
}
