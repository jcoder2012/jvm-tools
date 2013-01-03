// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package org.cloudera.jvmtools;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class PseudoBreakpoint {
  private Set<Class<?>> classesToTransform = new HashSet<Class<?>>();
  
  public static class Adapter extends ClassVisitor {
      public Adapter(ClassVisitor cv) {
          super(Opcodes.ASM4, cv);
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv;
        mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null) {
          mv = new MethodAdapter(mv);
        }
        return mv;
      }
  }

  public static class MethodAdapter extends MethodVisitor {
      public MethodAdapter(MethodVisitor mv) {
          super(Opcodes.ASM4, mv);
      }
      
      @Override
      public void visitCode() {
        // One suspects that this won't work for constructors!
        // When in doubt, the following is quite useful:
        // java -cp  ~/.m2/repository/org/ow2/asm/asm-all/4.0/asm-all-4.0.jar org.objectweb.asm.util.ASMifier core/eclipse-classes/org/cloudera/jvmtools/PsuedoBreakpointRuntime.class 
        System.err.println("x");
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, 
            "org/cloudera/jvmtools/PsuedoBreakpointRuntime", 
            "handle", 
            "()V");
      }
  }
  
  public class Transformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader l, String name, Class<?> c,
        ProtectionDomain d, byte[] b)
            throws IllegalClassFormatException {
      System.err.println("zz");
      synchronized(classesToTransform) {
        System.err.println("zz");
        if (classesToTransform.contains(c)) {
          System.err.println("yy");
          ClassReader cr = new ClassReader(b);
          ClassWriter cw = new ClassWriter(cr, 0);
          ClassVisitor cv = new Adapter(cw);
          cr.accept(cv, 0);
          return cw.toByteArray();
        } else {
          return b;
        }
      }
    }
    
  }
  
  public void setBreakpoint(Instrumentation inst, String className) 
      throws UnmodifiableClassException, ClassNotFoundException {
    System.err.println("y");
    Class<?> klass = Class.forName(className);
    synchronized(classesToTransform) {
      classesToTransform.add(klass);
    }
    // Should we handle multiple adds here?
    inst.addTransformer(new Transformer(), true /* can retransform */);
    System.err.println("z");
    System.err.println(klass);
    inst.retransformClasses(klass);
    System.err.println("u");
  }

}
