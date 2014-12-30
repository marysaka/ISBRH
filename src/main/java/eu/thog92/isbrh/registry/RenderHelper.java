package eu.thog92.isbrh.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;


public class RenderHelper {

	
	public static Tessellator initItemInventoryRender()
	{
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.setVertexFormat(DefaultVertexFormats.ITEM);
        return tessellator;
	}
	
	public static TextureAtlasSprite registerAndReloadBlockTextureMap(ResourceLocation textureLocation)
	{
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite sprite = map.getTextureExtry(textureLocation.toString());
		if(sprite != null) return sprite;
		
		//Get a TextureAtlas
		sprite = map.registerSprite(textureLocation);
		// Register texture
		map.setTextureEntry(textureLocation.toString(), sprite);
		// Buffer TextureMap
		map.loadTextureAtlas(Minecraft.getMinecraft().getResourceManager());
		return map.getTextureExtry(textureLocation.toString());
	}
}
