package starter.validate;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom ClassLoader that fetches .class files from a remote GitHub URL
 * and delegates unresolved classes to the parent loader (Serenity-aware).
 */
public class SerenityClassLoader extends ClassLoader {

    private final Map<String, Class<?>> cache = new HashMap<>();
    private final String baseUrl;

    public SerenityClassLoader(String baseUrl, ClassLoader parent) {
        super(parent);  // Delegate to parent loader for Serenity, Selenium, etc.
        this.baseUrl = baseUrl;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        try {
            String path = name.replace('.', '/') + ".class";
            String classUrl = baseUrl + "/" + path;

            System.out.println("🌐 Loading class: " + classUrl);
            HttpURLConnection conn = (HttpURLConnection) new URL(classUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            try (InputStream in = conn.getInputStream()) {
                byte[] bytes = in.readAllBytes();
                Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
                cache.put(name, clazz);
                return clazz;
            }
        } catch (Exception e) {
            throw new ClassNotFoundException("Failed to load " + name, e);
        }
    }
}