package eu.thog92.isbrh.coremod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class RITransformer implements ITransformHandler {

    @Override
    public byte[] transform(String className, byte[] buffer) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(buffer);
        classReader.accept(classNode, 0);
        List<MethodNode> methods = classNode.methods;
        Iterator<MethodNode> iterator = methods.iterator();
        MethodNode newMethod = null;
        String methodDesc = "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V";
        String transformType = "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType";
        String transformTypeNONE = "NONE";
        String transformTypeThirdPerson = "THIRD_PERSON";
        String transformTypeGUI = "GUI";
        String desc = "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V";
        String itemStackClass = "net/minecraft/item/ItemStack";
        String itemBlockClass = "net/minecraft/item/ItemBlock";
        String getItemName = "getItem";
        String getItemDesc = "()Lnet/minecraft/item/Item;";
        String getBlockName = "getBlock";
        String blockClass = "net/minecraft/block/Block";
        String getRenderTypeName = "getRenderType";
        String getRenderTypeDesc = "()I";
        String glStateManagerClass = "net/minecraft/client/renderer/GlStateManager";
        String pushMatrixName = "pushMatrix";
        String popMatrixName = "popMatrix";
        boolean ob;
        while (iterator.hasNext()) {
            MethodNode method = iterator.next();
            if ((method.name.equals("renderItem") && method.desc
                    .equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
                    || (method.name.equals("a") && method.desc
                    .equals("(Lamj;Lcxe;)V"))) {
                ob = method.name.equals("a");
                if (ob) {
                    transformType = "cmz";
                    desc = "(Lamj;Lcmz;)V";
                    itemStackClass = "amj";
                    itemBlockClass = "aju";
                    getItemName = "b";
                    getItemDesc = "()Lalq;";
                    getBlockName = "d";
                    getRenderTypeName = "b";
                    blockClass = "atr";
                    methodDesc = "(Lamj;Lcxe;Lcmz;)V";
                    transformTypeNONE = "a";
                    transformTypeThirdPerson = "b";
                    transformTypeGUI = "e";
                    glStateManagerClass = "cjm";
                    pushMatrixName = "E";
                    popMatrixName = "F";
                }
                newMethod = new MethodNode(Opcodes.ACC_PUBLIC, "renderItem", methodDesc, null, null);

                LabelNode label = new LabelNode();
                LabelNode label2 = new LabelNode();
                InsnList toInject = new InsnList();


                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, itemStackClass, getItemName, getItemDesc, false));
                toInject.add(new TypeInsnNode(Opcodes.INSTANCEOF, itemBlockClass));
                toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, itemStackClass, getItemName, getItemDesc, false));
                toInject.add(new TypeInsnNode(Opcodes.CHECKCAST, itemBlockClass));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, itemBlockClass, getBlockName, "()L" + blockClass + ";", false));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, blockClass, getRenderTypeName, getRenderTypeDesc, false));
                toInject.add(new InsnNode(Opcodes.ICONST_4));
                toInject.add(new JumpInsnNode(Opcodes.IF_ICMPLE, label));
                toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                        "eu/thog92/isbrh/registry/RenderRegistry",
                        "instance",
                        "()Leu/thog92/isbrh/registry/RenderRegistry;",
                        false));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
                toInject.add(new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        "eu/thog92/isbrh/registry/RenderRegistry",
                        "renderInventoryBlock", desc, false));
                toInject.add(new JumpInsnNode(Opcodes.GOTO, label2));
                toInject.add(label);

                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                        classNode.name,
                        method.name,
                        method.desc,
                        false));
                toInject.add(label2);
                toInject.add(new InsnNode(Opcodes.RETURN));
                newMethod.instructions.insert(toInject);

                System.out.println("Adding renderItem new method");


            }
            else if ((method.name.equals("renderItemModelTransform") && method.desc
                    .equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"))
                    || (method.name.equals("a") && method.desc
                    .equals("(Lamj;Lcxe;Lcmz;)V"))) {
                ob = method.name.equals("a");
                InsnList newInstruction = new InsnList();
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode abstractInsnNode = method.instructions
                            .get(i);
                    if (abstractInsnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                        if ((methodInsnNode.name.equals("renderItem") && methodInsnNode.desc
                                .equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
                                || (methodInsnNode.name.equals("a") && methodInsnNode.desc
                                .equals("(Lamj;Lcxe;)V"))) {
                            newInstruction
                                    .add(new VarInsnNode(Opcodes.ALOAD, 3));
                            newInstruction.add(new MethodInsnNode(
                                    Opcodes.INVOKEVIRTUAL,
                                    methodInsnNode.owner,
                                    newMethod.name, methodDesc, false));
                        } else
                            newInstruction.add(abstractInsnNode);
                    } else {
                        newInstruction.add(abstractInsnNode);
                    }
                }
                method.instructions = newInstruction;
            }
            else if ((method.name.equals("renderItemIntoGUI") && method.desc
                    .equals("(Lnet/minecraft/item/ItemStack;II)V"))
                    || (method.name.equals("a") && method.desc
                    .equals("(Lamj;II)V"))) {
                ob = method.name.equals("a");
                InsnList newInstruction = new InsnList();
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode abstractInsnNode = method.instructions
                            .get(i);
                    if (abstractInsnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                        if ((methodInsnNode.name.equals("renderItem") && methodInsnNode.desc
                                .equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
                                || (methodInsnNode.name.equals("a") && methodInsnNode.desc
                                .equals("(Lamj;Lcxe;)V"))) {

                            newInstruction.add(new FieldInsnNode(Opcodes.GETSTATIC,
                                    transformType, transformTypeGUI, "L" + transformType + ";"));
                            newInstruction.add(new MethodInsnNode(
                                    Opcodes.INVOKEVIRTUAL,
                                    methodInsnNode.owner,
                                    newMethod.name, methodDesc, false));
                        } else
                            newInstruction.add(abstractInsnNode);
                    } else {
                        newInstruction.add(abstractInsnNode);
                    }
                }
                method.instructions = newInstruction;
            }
            else if ((method.name.equals("shouldRenderItemIn3D") && method.desc
                    .equals("(Lnet/minecraft/item/ItemStack;)Z"))
                    || (method.name.equals("a") && method.desc
                    .equals("(Lamj;)Z"))) {
                ob = method.name.equals("a");
                String shouldRenderItemIn3DBodyDesc = "(Lnet/minecraft/client/renderer/entity/RenderItem;Lnet/minecraft/item/ItemStack;)Z";
                if (ob)
                    shouldRenderItemIn3DBodyDesc = "(Lcqh;Lamj;)Z";
                method.instructions.clear();
                method.localVariables = new ArrayList<LocalVariableNode>();
                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                method.instructions.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "eu/thog92/isbrh/coremod/RenderAccessHook",
                        "shouldRenderItemIn3DBody", shouldRenderItemIn3DBodyDesc, false));
                method.instructions.add(new InsnNode(Opcodes.IRETURN));
            }

        }
        classNode.methods.add(newMethod);


        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        byte[] patched = writer.toByteArray();
        try
        {
            FileOutputStream out = new FileOutputStream("RenderItem.class");
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
