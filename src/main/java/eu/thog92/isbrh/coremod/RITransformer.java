package eu.thog92.isbrh.coremod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class RITransformer implements ITransformHandler {

	@Override
	public byte[] transform(String className, byte[] buffer) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(buffer);
		classReader.accept(classNode, 0);
		List<MethodNode> methods = classNode.methods;
		Iterator<MethodNode> iterator = methods.iterator();

		while (iterator.hasNext()) {
			MethodNode method = iterator.next();

			if ((method.name.equals("renderItem") && method.desc
					.equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
					|| (method.name.equals("a") && method.desc
							.equals("(Lamj;Lcxe;)V"))) {
				System.out.println(method.name);
				boolean ob = method.name.equals("a");

				String transformType = "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType", desc = "(Lnet/minecraft/client/renderer/entity/RenderItem;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V";
				if (ob) {
					transformType = "cmz";
					desc = "(Lcqh;Lamj;Lcxe;Lcmz;)V";
				}

				method.instructions.clear();

				method.localVariables = new ArrayList<LocalVariableNode>();

				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
				method.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC,
						transformType, "NONE", "L" + transformType + ";"));
				method.instructions.add(new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"eu/thog92/isbrh/registry/RenderRegistry",
						"renderItemBody", desc, false));
				method.instructions.add(new InsnNode(Opcodes.RETURN));
			} else if ((method.name.equals("renderItemModelTransform") && method.desc
					.equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"))
					|| (method.name.equals("a") && method.desc
							.equals("(Lamj;Lcxe;Lcmz;)V"))) {
				boolean ob = method.name.equals("a");
				String desc = "(Lnet/minecraft/client/renderer/entity/RenderItem;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V";
				if (ob) {
					desc = "(Lcqh;Lamj;Lcxe;Lcmz;)V";
				}
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
									Opcodes.INVOKESTATIC,
									"eu/thog92/isbrh/registry/RenderRegistry",
									"renderItemBody", desc, false));
						} else
							newInstruction.add(abstractInsnNode);
					} else {
						newInstruction.add(abstractInsnNode);
					}
				}

				method.instructions = newInstruction;
			} else if ((method.name.equals("shouldRenderItemIn3D") && method.desc
					.equals("(Lnet/minecraft/item/ItemStack;)Z"))
					|| (method.name.equals("a") && method.desc
							.equals("(Lamj;)Z"))) {
				boolean ob = method.name.equals("a");
				String desc = "(Lnet/minecraft/client/renderer/entity/RenderItem;Lnet/minecraft/item/ItemStack;)Z";
				if (ob)
					desc = "(Lcqh;Lamj;)Z";
				method.instructions.clear();
				method.localVariables = new ArrayList<LocalVariableNode>();
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
				method.instructions.add(new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"eu/thog92/isbrh/registry/RenderRegistry",
						"shouldRenderItemIn3DBody", desc, false));
				method.instructions.add(new InsnNode(Opcodes.IRETURN));
			}

		}

		ClassWriter writer = new ClassWriter(0);
		classNode.accept(writer);
		byte[] patched = writer.toByteArray();
		FileOutputStream out;
		try {
			out = new FileOutputStream("RenderItem.class");
			out.write(patched);

			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return patched;
	}

}
