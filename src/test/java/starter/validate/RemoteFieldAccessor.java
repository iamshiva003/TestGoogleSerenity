package starter.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Utility class to dynamically get or set instance/static fields
 * on classes loaded via SerenityClassLoader or any other ClassLoader.
 */
public class RemoteFieldAccessor {

    private static ClassLoader loader = null;

    public RemoteFieldAccessor(ClassLoader loader) {
        RemoteFieldAccessor.loader = loader;
    }

    /**
     * Reads a field value from a given class or object.
     * Works for both static and instance fields.
     */
    public static Object getFieldValue(String className, String fieldName, Object instance) throws Exception {
        Class<?> clazz = loader.loadClass(className);
        Field field = findField(clazz, fieldName);

        field.setAccessible(true);

        if (Modifier.isStatic(field.getModifiers())) {
            return field.get(null); // static field
        } else {
            return field.get(instance); // instance field
        }
    }

    /**
     * Updates a field value (static or instance).
     */
    public void setFieldValue(String className, String fieldName, Object instance, Object newValue) throws Exception {
        Class<?> clazz = loader.loadClass(className);
        Field field = findField(clazz, fieldName);

        field.setAccessible(true);

        if (Modifier.isStatic(field.getModifiers())) {
            field.set(null, newValue);
        } else {
            field.set(instance, newValue);
        }

        System.out.printf("🧩 Field '%s' in %s updated to: %s%n", fieldName, className, newValue);
    }

    /**
     * Helper to find field in class or its superclasses.
     */
    private static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field not found: " + fieldName);
    }
}