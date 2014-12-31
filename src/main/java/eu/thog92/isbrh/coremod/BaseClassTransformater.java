package eu.thog92.isbrh.coremod;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

	public byte[] patchClass(String name, byte[] bytes, File location) {
		try {
			ZipFile zip = new ZipFile(location);
			ZipEntry entry = zip.getEntry(name.replace('.', '/') + ".class");
			if (entry == null) {
				System.out.println("[ISBRH]:" + name + " not found in "
						+ location.getName());
			} else {
				// serialize the class file into the bytes array
				InputStream zin = zip.getInputStream(entry);
				bytes = new byte[(int) entry.getSize()];
				zin.read(bytes);
				zin.close();
				System.out.println("[ISBRH]: " + "Class " + name + " patched!");
			}
			zip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

}
