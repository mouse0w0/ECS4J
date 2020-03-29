package com.github.mouse0w0.ecs.system.invoker;

import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.util.SafeClassDefiner;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.objectweb.asm.Opcodes.*;

public class AsmSystemInvokerFactory implements SystemInvokerFactory {
    private final SafeClassDefiner classDefiner = new SafeClassDefiner();
    private final AtomicInteger nextId = new AtomicInteger();
    private final Map<Method, Class<?>> cachedClasses = Collections.synchronizedMap(new HashMap<>());
    private final String factoryHash = Integer.toHexString(System.identityHashCode(this));

    @Override
    public SystemInvoker create(Object owner, Method method, ComponentMapper[] mappers) {
        Class<?> invokerClass = getInvokerClass(owner, method);
        Constructor<?> constructor = invokerClass.getDeclaredConstructors()[0];
        Object[] args = new Object[constructor.getParameterCount()];
        args[0] = owner;
        System.arraycopy(mappers, 1, args, 1, args.length - 1);
        try {
            return (SystemInvoker) constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String[] INTERFACES = {Type.getInternalName(SystemInvoker.class)};

    private static final String[] EXCEPTIONS = {Type.getInternalName(Exception.class)};

    private static final String COMPONENT_MAPPER_INTERNAL_NAME = Type.getInternalName(ComponentMapper.class);
    private static final String COMPONENT_MAPPER_DESC = Type.getDescriptor(ComponentMapper.class);
    private static final String COMPONENT_MAPPER_GET_METHOD_NAME = "get";
    private static final String COMPONENT_MAPPER_GET_METHOD_DESC;

    private static final String METHOD_UPDATE_NAME = "update";
    private static final String METHOD_UPDATE_DESC;

    static {
        try {
            Method getMethod = ComponentMapper.class.getDeclaredMethod(COMPONENT_MAPPER_GET_METHOD_NAME, int.class);
            COMPONENT_MAPPER_GET_METHOD_DESC = Type.getMethodDescriptor(getMethod);

            Method methodUpdate = SystemInvoker.class.getDeclaredMethod(METHOD_UPDATE_NAME, int.class);
            METHOD_UPDATE_DESC = Type.getMethodDescriptor(methodUpdate);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public Class<?> getInvokerClass(Object owner, Method method) {
        if (cachedClasses.containsKey(method)) {
            return cachedClasses.get(method);
        }

        boolean isStatic = Modifier.isStatic(method.getModifiers());
        Class<?> ownerClass = isStatic ? (Class<?>) owner : owner.getClass();
        String ownerInternalName = Type.getInternalName(ownerClass);
        String ownerDesc = Type.getDescriptor(ownerClass);
        String methodName = method.getName();
        String methodDesc = Type.getMethodDescriptor(method);
        int parameterCount = method.getParameterCount();
        int componentCount = parameterCount - 1;
        String invokerName = getInvokeName(ownerClass.getSimpleName(), methodName);

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, invokerName, null, "java/lang/Object", INTERFACES);
        cw.visitSource(".dynamic", null);

        // Define fields.
        if (!isStatic) {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "owner", ownerDesc, null, null);
            fv.visitEnd();
        }

        for (int i = 0; i < componentCount; i++) {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "mapper" + i, COMPONENT_MAPPER_DESC, null, null);
            fv.visitEnd();
        }

        // Define constructor.
        {
            String desc = componentCount == 0 ? String.format("(%s)V", ownerDesc) :
                    String.format("(%s%s)V", ownerDesc, COMPONENT_MAPPER_DESC.repeat(componentCount));
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", desc, desc, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            if (!isStatic) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitFieldInsn(PUTFIELD, invokerName, "owner", ownerDesc);
            }
            for (int i = 0; i < componentCount; i++) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, i + 2);
                mv.visitFieldInsn(PUTFIELD, invokerName, "mapper" + i, COMPONENT_MAPPER_DESC);
            }
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, parameterCount + 1);
            mv.visitEnd();
        }

        // Define update method
        {
            mv = cw.visitMethod(ACC_PUBLIC, METHOD_UPDATE_NAME, METHOD_UPDATE_DESC, null, EXCEPTIONS);
            if (!isStatic) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, invokerName, "owner", ownerDesc);
            }
            mv.visitVarInsn(ILOAD, 1);

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < componentCount; i++) {
                Class<?> componentType = parameterTypes[i + 1];
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, invokerName, "mapper" + i, COMPONENT_MAPPER_DESC);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitMethodInsn(INVOKEVIRTUAL, COMPONENT_MAPPER_INTERNAL_NAME,
                        COMPONENT_MAPPER_GET_METHOD_NAME, COMPONENT_MAPPER_GET_METHOD_DESC, false);
                mv.visitTypeInsn(CHECKCAST, Type.getInternalName(componentType));
            }

            mv.visitMethodInsn(isStatic ? INVOKESTATIC : INVOKEVIRTUAL, ownerInternalName, methodName, methodDesc, false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(parameterCount + 2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        // Define class
        Class<?> invokerClass = classDefiner.defineClass(ownerClass.getClassLoader(), invokerName, cw.toByteArray());
        cachedClasses.put(method, invokerClass);
        return invokerClass;
    }

    private String getInvokeName(String ownerName, String methodName) {
        return "SystemInvoker_" + factoryHash + "_" + ownerName + "_" + methodName + "_" + nextId.getAndIncrement();
    }
}
