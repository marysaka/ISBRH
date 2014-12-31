package eu.thog92.isbrh.coremod;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BRDTransformer implements ITransformHandler {

	@Override
	public byte[] transform(String className, byte[] buffer) {

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(buffer);
		classReader.accept(classNode, 0);
		List<MethodNode> methods = classNode.methods;
		Iterator<MethodNode> iterator = methods.iterator();
		int iconst = 0;
		while (iterator.hasNext()) {
			MethodNode m = iterator.next();
			boolean obfuscated;
			if ((m.name.equals("renderBlock") && m.desc
					.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/WorldRenderer;)Z"))
					|| (m.name.equals("a") && m.desc
							.equals("(Lbec;Ldt;Lard;Lciv;)Z"))) {
				InsnList toInject = new InsnList();
				obfuscated = m.name.equals("a");
				String desc;
				if (obfuscated)
					desc = "(ILbec;Ldt;Lard;Lciv;)Z";
				else
					desc = "(ILnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/WorldRenderer;)Z";
				for (int i = 0; i < m.instructions.size(); i++) {
					AbstractInsnNode insn = m.instructions.get(i);
					if (insn.getOpcode() == Opcodes.ICONST_0) {
						iconst++;
						if (iconst != 3)
							continue;

						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
								"eu/thog92/isbrh/registry/RenderRegistry",
								"instance",
								"()Leu/thog92/isbrh/registry/RenderRegistry;",
								false));
						toInject.add(new VarInsnNode(Opcodes.ILOAD, 5));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 4));
						toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
								"eu/thog92/isbrh/registry/RenderRegistry",
								"renderBlock", desc, false));

						m.instructions.insertBefore(insn, toInject);

					}
				}
			}
			if ((m.name.equals("renderBlockBrightness") && m.desc
					.equals("(Lnet/minecraft/block/state/IBlockState;F)V"))
					|| (m.name.equals("a") && m.desc.equals("(Lbec;F)V"))) {
				InsnList toInject = new InsnList();
				obfuscated = m.name.equals("a");
				String desc = "(ILnet/minecraft/block/state/IBlockState;F)V";
				if (obfuscated) {
					desc = "(ILbec;F)V";
				}
				for (int i = 0; i < m.instructions.size(); i++) {
					AbstractInsnNode insn = m.instructions.get(i);
					if (insn.getOpcode() == Opcodes.GOTO) {
						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
								"eu/thog92/isbrh/registry/RenderRegistry",
								"instance",
								"()Leu/thog92/isbrh/registry/RenderRegistry;",
								false));
						toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
						toInject.add(new VarInsnNode(Opcodes.FLOAD, 2));
						toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
								"eu/thog92/isbrh/registry/RenderRegistry",
								"renderBlockBrightness", desc, false));
						m.instructions.insertBefore(insn, toInject);
						break;
					}
				}

			}
		}
		ClassWriter writer = new ClassWriter(0);
		classNode.accept(writer);
		byte[] patched = writer.toByteArray();
		return patched;
	}

}
