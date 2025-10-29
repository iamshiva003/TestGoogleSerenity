package starter.validate;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Dynamically loads and invokes Serenity methods remotely (e.g. from GitHub).
 * Handles return values, void methods, static methods, PageObject injection, etc.
 */
public class RemoteInvokerAdvanced {

    // ✅ Base path to your compiled .class files (raw GitHub link)
    private static final String GITHUB_RAW_BASE =
            "https://raw.githubusercontent.com/iamshiva003/TestGoogleSerenity/master/build/classes/java/test/";

    // ✅ Serenity-aware custom classloader
    private static final SerenityClassLoader loader =
            new SerenityClassLoader(GITHUB_RAW_BASE, Thread.currentThread().getContextClassLoader());

    static {
        Thread.currentThread().setContextClassLoader(loader);
        System.out.println("🧠 SerenityClassLoader set as context loader");
    }

    /**
     * Invokes any remote method safely.
     *
     * @param className fully qualified class name
     * @param methodName method to invoke
     * @param args optional arguments
     * @return the result of the method, or null if void or failed
     */
    public static Object invoke(String className, String methodName, Object... args) {
        try {
            // ✅ Load class dynamically
            Class<?> clazz = loader.loadClass(className);

            // ✅ Find method by name and arg count
            Method method = findMethod(clazz, methodName, args);
            method.setAccessible(true);

            Object instance = null;

            // ✅ If non-static → instantiate
            if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                instance = clazz.getDeclaredConstructor().newInstance();

                // ✅ Handle Serenity PageObjects
                if (instance instanceof PageObject page) {
                    WebDriver driver = ThucydidesWebDriverSupport.getDriver();

                    // If Serenity context hasn't been initialized yet
                    if (driver == null) {
                        ThucydidesWebDriverSupport.initialize();  // this sets up the driver internally
                        driver = ThucydidesWebDriverSupport.getDriver();
                    }

                    // Assign driver to PageObject and initialize Serenity context
                    page.setDriver(driver);
                    Serenity.initialize(page);
                }
            }

            // ✅ Invoke and handle return
            Object result = method.invoke(instance, args);

            System.out.printf("✅ Invoked %s.%s()%n", className, methodName);

            if (method.getReturnType() != void.class) {
                System.out.println("🔙 Returned: " + result);
            }

            return result;

        } catch (InvocationTargetException e) {
            // Method threw an exception
            System.err.printf("❌ Exception inside invoked method: %s%n",
                    e.getTargetException().getMessage());
            e.getTargetException().printStackTrace();
        } catch (Exception e) {
            System.err.printf("❌ Failed to invoke %s.%s(): %s%n",
                    className, methodName, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static Method findMethod(Class<?> clazz, String name, Object... args) throws NoSuchMethodException {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == args.length) {
                return m;
            }
        }
        throw new NoSuchMethodException(name);
    }
}