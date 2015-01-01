package eu.thog92.isbrh;

import eu.thog92.isbrh.registry.RenderRegistry;
import eu.thog92.isbrh.test.BlockTest;
import eu.thog92.isbrh.test.RenderTest;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "isbrhcore", name = "ISBRH Core", acceptedMinecraftVersions = "[1.8]")
public class ISBRH {

    public static final Block test = new BlockTest().setUnlocalizedName("test");
    public static final ResourceLocation textureLocation = new ResourceLocation(
            "isbrhcore:blocks/test");
    public static int testId;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        testId = RenderRegistry.getNextAvailableRenderId();
        GameRegistry.registerBlock(test, "test");
        RenderRegistry.registerBlockHandler(new RenderTest());
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.map == Minecraft.getMinecraft().getTextureMapBlocks()) {
            RenderRegistry.instance().injectTexture(event.map);
        }
    }
}
