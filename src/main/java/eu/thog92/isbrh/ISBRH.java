package eu.thog92.isbrh;

import eu.thog92.isbrh.example.BlockExample;
import eu.thog92.isbrh.example.RenderExample;
import eu.thog92.isbrh.registry.RenderRegistry;
import eu.thog92.isbrh.render.ITextureHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

@Mod(modid = "isbrhcore", name = "ISBRH", clientSideOnly = true, acceptedMinecraftVersions = "[1.8]")
public class ISBRH
{
    public static final Block test = new BlockExample()
            .setUnlocalizedName("test");
    public static final ResourceLocation textureLocation = new ResourceLocation(
            "isbrhcore:blocks/test");
    public static int testId;
    private static boolean exempleEnabled = Boolean.parseBoolean(System
            .getProperty("isbrhCore.enableExemple", "false"));
    private Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (exempleEnabled)
        {
            logger.info("Registering ISBRH Block Exemple...");
            testId = RenderRegistry.getNextAvailableRenderId();
            GameRegistry.registerBlock(test, "test");
            RenderRegistry.registerBlockHandler(new RenderExample());
            RenderRegistry.registerTextureHandler((ITextureHandler) test);
        }

        Minecraft mc = Minecraft.getMinecraft();

        // Prerequisite
        ModelManager modelManager = ObfuscationReflectionHelper
                .getPrivateValue(Minecraft.class, mc, "aL", "field_175617_aL",
                        "modelManager");
        RenderManager renderManager = mc.getRenderManager();
        IReloadableResourceManager resourceManager = ((IReloadableResourceManager) mc
                .getResourceManager());

        // Render Item Hook
        RenderItem item = new RenderItemISBRH(mc.getTextureManager(),
                modelManager);
        ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc, item,
                "X", "field_175621_X", "renderItem");
        ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc,
                new ItemRenderer(mc), "Y", "field_175620_Y", "itemRenderer");
        renderManager.entityRenderMap.remove(EntityItem.class);
        renderManager.entityRenderMap.put(EntityItem.class,
                new RenderEntityItem(renderManager, item));
        renderManager.entityRenderMap.remove(EntityItemFrame.class);
        renderManager.entityRenderMap.put(EntityItemFrame.class,
                new RenderItemFrame(renderManager, item));
        mc.entityRenderer = new EntityRenderer(mc, resourceManager);

        // Render Block Dispatcher Hook
        BlockRendererDispatcher rendererDispatcher = new ISBRHDispatcher(
                modelManager.getBlockModelShapes(), mc.gameSettings);
        ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc,
                rendererDispatcher, "aM", "field_175618_aM",
                "blockRenderDispatcher");

        // Register Reload Listeners
        resourceManager.registerReloadListener(rendererDispatcher);
        resourceManager.registerReloadListener(item);
        resourceManager.registerReloadListener(mc.entityRenderer);

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        if (event.map == Minecraft.getMinecraft().getTextureMapBlocks())
        {
            RenderRegistry.instance().injectTexture(event.map);
        }
    }
}
