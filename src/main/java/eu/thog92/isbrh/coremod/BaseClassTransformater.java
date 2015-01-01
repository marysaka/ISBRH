package eu.thog92.isbrh.coremod;

import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

public class BaseClassTransformater implements IClassTransformer {

	private BiMap<String, String> classToPatch = HashBiMap
			.create(new HashMap<String, String>());
	private HashMap<String, ITransformHandler> handlers = Maps.newHashMap();

	public BaseClassTransformater() {
		classToPatch.put(
				"net.minecraft.client.renderer.BlockRendererDispatcher", "cll");
		classToPatch.put("net.minecraft.client.renderer.entity.RenderItem",
				"cqh");
		handlers.put("net.minecraft.client.renderer.BlockRendererDispatcher",
				new BRDTransformer());
		handlers.put("net.minecraft.client.renderer.entity.RenderItem",
				new RITransformer());

	}

	@Override
	public byte[] transform(String contextName, String transformedName,
			byte[] bytes) {
		boolean isKey = classToPatch.containsKey(contextName);
		boolean isValue = classToPatch.containsValue(contextName);
		if (isKey || isValue) {
			String name = contextName;
			if (isValue)
				name = classToPatch.inverse().get(name);
			if (handlers.get(name) == null)
				return bytes;
			return handlers.get(name).transform(contextName, bytes);
		}
		return bytes;
	}

}
