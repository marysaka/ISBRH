package eu.thog92.isbrh.coremod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thog92 on 02/01/2015.
 */
public class RenderEntityItemTransformer implements ITransformHandler {
    @Override
    public byte[] transform(String className, byte[] buffer) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(buffer);
        classReader.accept(classNode, 0);
        List<MethodNode> methods = classNode.methods;
        Iterator<MethodNode> iterator = methods.iterator();
        String transformType = "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType";
        String methodDesc = "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V";
        String transformTypeNONE = "NONE";
        while(iterator.hasNext())
        {
            MethodNode targetMethod = iterator.next();
            if ((targetMethod.name.equals("func_177075_a") && targetMethod.desc
                    .equals("(Lnet/minecraft/entity/item/EntityItem;DDDFF)V"))
                    || (targetMethod.name.equals("a") && targetMethod.desc
                    .equals("(Ladw;DDDFF)V"))) {
                boolean ob = targetMethod.name.equals("a");
                if(ob)
                {
                    transformType = "cmz";
                    transformTypeNONE = "a";
                    methodDesc = "(Lamj;Lcxe;Lcmz;)V";
                }
                InsnList newInstruction = new InsnList();
                for (int i = 0; i < targetMethod.instructions.size(); i++) {
                    AbstractInsnNode abstractInsnNode = targetMethod.instructions
                            .get(i);
                    if (abstractInsnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                        if ((methodInsnNode.name.equals("renderItem") && methodInsnNode.desc
                                .equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
                                || (methodInsnNode.name.equals("a") && methodInsnNode.desc
                                .equals("(Lamj;Lcxe;)V"))) {

                            newInstruction.add(new FieldInsnNode(Opcodes.GETSTATIC,
                                    transformType, transformTypeNONE, "L" + transformType + ";"));
                            newInstruction.add(new MethodInsnNode(
                                    Opcodes.INVOKEVIRTUAL,
                                    methodInsnNode.owner,
                                    "renderItem", methodDesc, false));
                        } else
                            newInstruction.add(abstractInsnNode);
                    } else {
                        newInstruction.add(abstractInsnNode);
                    }
                }
                targetMethod.instructions = newInstruction;
            }
        }
        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        byte[] patched = writer.toByteArray();
        try
        {
            FileOutputStream out = new FileOutputStream("RenderEntityItem.class");
            out.write(patched);
            out.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return patched;
    }
}
