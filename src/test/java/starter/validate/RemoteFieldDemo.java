package starter.validate;

import java.lang.reflect.Constructor;

/**
 * Demonstrates dynamic field access using SerenityClassLoader + RemoteFieldAccessor.
 *
 * This class:
 *  1. Dynamically loads a class from GitHub (ConfigUtils.class)
 *  2. Modifies static and instance variables
 *  3. Reads values back using reflection
 */
public class RemoteFieldDemo {

    // 🔗 GitHub RAW base for your compiled .class files
    private static final String GITHUB_RAW_BASE =
            "https://raw.githubusercontent.com/iamshiva003/SalesWizzAutomation/refs/heads/main/build/classes/java/test/";

    public static void main(String[] args) throws Exception {

        // 1️⃣ Create Serenity-aware custom class loader
        SerenityClassLoader loader =
                new SerenityClassLoader(GITHUB_RAW_BASE, Thread.currentThread().getContextClassLoader());

        // 2️⃣ Create our field accessor utility
        RemoteFieldAccessor fieldAccessor = new RemoteFieldAccessor(loader);

        // 3️⃣ Modify a static variable (class-level)
//        fieldAccessor.setFieldValue("automation.ConfigUtils", "baseUrl", null, "https://newsite.com");

        // 4️⃣ Create an instance dynamically
        Class<?> configClass = loader.loadClass("starter.check.ClassReturns");
        Constructor<?> constructor = configClass.getDeclaredConstructor();
        Object configInstance = constructor.newInstance();

        // 5️⃣ Modify an instance variable (object-level)

        // 6️⃣ Read field values
//        Object staticValue = fieldAccessor.getFieldValue("starter.check.ClassReturns", "lastname", null);

        fieldAccessor.setFieldValue("starter.check.ClassReturns", "age", configInstance, 15);
        Object retryValue = fieldAccessor.getFieldValue("starter.check.ClassReturns", "age", configInstance);

//        re assigning and checking
        fieldAccessor.setFieldValue("starter.check.ClassReturns", "age", configInstance, 155);
        retryValue = fieldAccessor.getFieldValue("starter.check.ClassReturns", "age", configInstance);

        Object name = fieldAccessor.getFieldValue("starter.check.ClassReturns", "name", configInstance);

        // 7️⃣ Print results
//        System.out.println("🌐 baseUrl (static): " + staticValue);
        System.out.println("🔁 retryCount (instance): " + retryValue);
        System.out.println("🔁 retryCount name (instance): " + name);

        System.out.println("✅ Field access and modification completed successfully!");
    }
}