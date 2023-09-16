package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonCompatibilityUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonCompatibilityUtil.class);

    private static final Set<String> unsupportedClasses = new HashSet<>(Arrays.asList("java.util.Optional", "java.util.Calendar", "java.util.Iterator", "java.io.InputStream", "java.io.OutputStream"));


    /**
     * Determine whether a Class can be serialized by JSON.
     * @param clazz Incoming Class.
     * @return If a Class can be serialized by JSON, return true;
     * else return false.
     */
    public static boolean checkClassCompatibility(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        boolean result;

        for (Method method : methods) {
            result = checkMethodCompatibility(method);
            if (!result) {
                return false;
            }
        }
        return true;
    }


    /**
     * Determine whether a Method can be serialized by JSON.
     * @param method Incoming Method.
     * @return If a Method can be serialized by JSON, return true;
     * else return false.
     */
    public static boolean checkMethodCompatibility(Method method) {

        boolean result;

        Type[] types = method.getGenericParameterTypes();
        List<Type> typeList = new ArrayList<>(Arrays.asList(types));
        Type returnType =  method.getGenericReturnType();
        typeList.add(returnType);
        for (Type type : typeList) {
            result = checkType(type);
            if (!result) {
                return false;
            }
        }

        return true;
    }


    /**
     * Get unsupported methods.
     * @param clazz
     * @return If there are unsupported methods, return them by List;
     * else return null.
     */
    public static List<String> getUnsupportedMethods(Class<?> clazz) {
        ArrayList<String> unsupportedMethods = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (!checkMethodCompatibility(method)) {
                unsupportedMethods.add(method.getName());
            }
        }

        return unsupportedMethods.size() > 0 ? unsupportedMethods : null;
    }


    /**
     * Determine whether a Type can be serialized by JSON.
     * @param classType Incoming Type.
     * @return If a Type can be serialized by JSON, return true;
     * else return false.
     */
    private static boolean checkType(Type classType) {

        boolean result;

        if (classType instanceof TypeVariable) {
            return true;
        }

        if (classType instanceof ParameterizedType) {

            Type[] types = ((ParameterizedType) classType).getActualTypeArguments();
            List<Type> typeList = new ArrayList<>(Arrays.asList(types));
            classType = ((ParameterizedType) classType).getRawType();
            typeList.add(classType);
            for (Type type : typeList) {
                result = checkType(type);
                if (!result) {
                    return false;
                }
            }
        } else if (classType instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) classType).getGenericComponentType();
            result = checkType(componentType);
            return result;
        } else if (classType instanceof Class<?> ) {
            Class<?> clazz = (Class<?>) classType;

            String className = clazz.getName();

            if (clazz.isArray()) {
                Type componentType = clazz.getComponentType();
                result = checkType(componentType);
                return result;
            } else if (clazz.isPrimitive()) {
                // deal with case of basic byte
                return !unsupportedClasses.contains(className);
            } else if (className.startsWith("java") || className.startsWith("javax")) {
                return !unsupportedClasses.contains(className);
            } else {
                // deal with case of interface
                if (clazz.isInterface()) {
                    return false;
                }
                // deal with case of abstract
                if (Modifier.isAbstract(clazz.getModifiers())) {
                    return false;
                }
                if (clazz.isEnum()) {
                    return true;
                }
                // deal with case of record
//                if (clazz.isRecord()) {
//                    return false;
//                }
                // deal with field one by one
                for (Field field : clazz.getDeclaredFields()) {
                    Type type = field.getGenericType();
                    Class<?> fieldClass = field.getType();
                    result = checkType(type);
                    if (!result) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
