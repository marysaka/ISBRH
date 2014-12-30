package eu.thog92.isbrh.coremod;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class BRDTransformer implements ITransformHandler {

	@Override
	public byte[] transform(String className, byte[] buffer) {

		InsnList toInject = new InsnList();
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(buffer);
		classReader.accept(classNode, 0);
		List<MethodNode> methods = classNode.methods;
		Iterator<MethodNode> iterator = methods.iterator();
		while (iterator.hasNext()) {
			MethodNode m = iterator.next();
			if (m.name.equals("renderBlock")) {
				for (int i = 0; i < m.instructions.size(); i++) {
					AbstractInsnNode insn = m.instructions.get(i);
					System.out.println(insn + " " +  Integer.toHexString(insn.getOpcode()));
					if(insn instanceof FieldInsnNode)
					{
						FieldInsnNode field = (FieldInsnNode)insn;
						System.out.println(field.name);
					}
				}
			}
		}
		return buffer;
	}

}
