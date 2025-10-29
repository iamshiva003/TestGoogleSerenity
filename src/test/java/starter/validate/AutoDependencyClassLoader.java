package starter.validate;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Method;

public class AutoDependencyClassLoader extends ClassLoader {

    // GitHub raw base URL — use raw.githubusercontent.com format
    private static final String BASE_URL =
            "https://github.com/iamshiva003/TestGoogleSerenity/raw/refs/heads/master/build/classes/java/test/starter/check/";

    public AutoDependencyClassLoader(ClassLoader parent) {
        super(parent);
    }

    // --- 1️⃣ Fetch and define a class dynamically when requested ---
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // Convert package name to file path
            String simpleName = name.substring(name.lastIndexOf('.') + 1);
            String url = BASE_URL + simpleName + ".class";

            System.out.println("🔄 Loading class dynamically from: " + url);

            byte[] bytes = fetchClassBytes(url);
            if (bytes == null) {
                throw new ClassNotFoundException("Could not load " + name);
            }

            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException("Failed to load " + name, e);
        }
    }

    // --- 2️⃣ Helper to fetch bytes from GitHub ---
    private byte[] fetchClassBytes(String urlStr) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/octet-stream");

        try (InputStream in = connection.getInputStream()) {
            return in.readAllBytes();
        }
    }

    // --- 3️⃣ Demo main method ---
    public static void main(String[] args) throws Exception {
        AutoDependencyClassLoader loader = new AutoDependencyClassLoader(ClassLoader.getSystemClassLoader());

        // Load Test class (this will automatically pull Test1 when used)
        Class<?> testClass = loader.loadClass("starter.check.Navigate");

        // Call static display() method
        Method display = testClass.getDeclaredMethod("display");
        display.invoke(testClass.getDeclaredConstructor().newInstance());

        System.out.println("✅ Successfully invoked automation.Test.display()");
    }
}