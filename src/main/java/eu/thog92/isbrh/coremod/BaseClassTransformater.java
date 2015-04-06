package eu.thog92.isbrh.coremod;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import net.minecraft.launchwrapper.IClassTransformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class BaseClassTransformater implements IClassTransformer {

    private BiMap<String, String> classToPatch = HashBiMap
            .create(new HashMap<String, String>());
    private HashMap<String, ITransformHandler> handlers = Maps.newHashMap();
    
    private static boolean debugASM = Boolean.parseBoolean(System.getProperty("isbrhCore.debugASM", "false"));

    public BaseClassTransformater() {
        classToPatch.put(
                "net.minecraft.client.renderer.BlockRendererDispatcher", "cll");
        classToPatch.put("net.minecraft.client.renderer.entity.RenderItem",
                "cqh");
        classToPatch.put("net.minecraft.client.renderer.entity.RenderEntityItem", "cqf");
        handlers.put("net.minecraft.client.renderer.BlockRendererDispatcher",
                new BRDTransformer());
        handlers.put("net.minecraft.client.renderer.entity.RenderItem",
                new RITransformer());
        handlers.put("net.minecraft.client.renderer.entity.RenderEntityItem", new RenderEntityItemTransformer());

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
            
            byte[] patchedBytes = handlers.get(name).transform(contextName, bytes);
            if(debugASM)
            {
                File outDir = new File("asm/isbrhcore");
                outDir.mkdirs();
                File outFile = new File(outDir, transformedName + ".class");
                FileOutputStream outStream;
                try
                {
                    outStream = new FileOutputStream(outFile);
                    outStream.write(patchedBytes, 0, patchedBytes.length);
                    outStream.flush();
                    outStream.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
            return patchedBytes;
        }
        
        return bytes;
    }

}
