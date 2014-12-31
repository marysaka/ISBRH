package eu.thog92.isbrh.coremod;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BRDTransformer implements ITransformHandler {

	@Override
	public byte[] transform(String className, byte[] buffer) {

		InsnList toInject = new InsnList();
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(buffer);
		classReader.accept(classNode, 0);
		List<MethodNode> methods = classNode.methods;
		Iterator<MethodNode> iterator = methods.iterator();
		int iconst = 0;
		while (iterator.hasNext()) {
			MethodNode m = iterator.next();
			System.out.println(m.name);
			if (m.name.equals("renderBlock")) {
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
						toInject.add(new MethodInsnNode(
								Opcodes.INVOKEVIRTUAL,
								"eu/thog92/isbrh/registry/RenderRegistry",
								"renderBlock",
								"(ILnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/WorldRenderer;)Z",
								false));

						m.instructions.insertBefore(insn, toInject);

						ClassWriter writer = new ClassWriter(0);
						classNode.accept(writer);
						byte[] patched = writer.toByteArray();
						return patched;
					}
				}
			}
		}
		return buffer;
	}

}
