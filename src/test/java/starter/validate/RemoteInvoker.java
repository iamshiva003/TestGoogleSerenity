package starter.validate;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;

/**
 * Dynamically loads and invokes methods from remote Serenity classes (e.g., from GitHub).
 * Handles WebDriver setup, PageObject injection, and method invocation automatically.
 */
public class RemoteInvoker {

    // 👇 Base URL to your compiled class files (RAW GitHub base path)
    private static final String GITHUB_RAW_BASE =
            "https://github.com/iamshiva003/TestGoogleSerenity/raw/refs/heads/master/build/classes/java/test/";

    // 👇 Serenity-aware ClassLoader
    private static final SerenityClassLoader loader =
            new SerenityClassLoader(GITHUB_RAW_BASE, Thread.currentThread().getContextClassLoader());

    static {
        // Make this the context loader so Serenity uses it
        Thread.currentThread().setContextClassLoader(loader);
        System.out.println("🧠 SerenityClassLoader set as context loader");
    }

    /**
     * Dynamically invokes a method from a remote class (with Serenity context).
     */
    public static void invoke(String className, String methodName, Object... args) throws Exception {
        // 1️⃣ Load class dynamically from remote repo
        Class<?> clazz = loader.loadClass(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // 2️⃣ Ensure WebDriver exists
        WebDriver driver = ThucydidesWebDriverSupport.getDriver();
        if (driver == null) {
            ThucydidesWebDriverSupport.initialize(); // creates driver context
            driver = ThucydidesWebDriverSupport.getDriver();
            System.out.println("🌐 Created new Serenity WebDriver session");
        }

        // 3️⃣ If it's a Serenity PageObject, wire driver + init elements
        if (instance instanceof PageObject page) {
            page.setDriver(driver);
            Serenity.initialize(page);
            System.out.println("🧩 PageObject initialized by Serenity");
        }

        // 4️⃣ Find and invoke the target method
        Method method = findMethod(clazz, methodName, args);
        method.setAccessible(true);
        method.invoke(instance, args);

        System.out.println("✅ Successfully invoked " + className + "." + methodName);
    }

    /**
     * Utility to locate a method by name and argument count.
     */
    private static Method findMethod(Class<?> clazz, String name, Object... args) throws NoSuchMethodException {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == args.length) {
                return m;
            }
        }
        throw new NoSuchMethodException(name);
    }
}