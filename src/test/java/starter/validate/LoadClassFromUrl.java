package starter.validate;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Method;

// Minimal class loader that can define a class from bytes
class ByteArrayClassLoader extends ClassLoader {
    public Class<?> defineClassFromBytes(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }
}

public class LoadClassFromUrl {
    public static void main(String[] args) throws Exception {
        String classUrl = "http://localhost:8080/get-assertion-utils"; // your endpoint

        // Fetch .class bytes
        HttpURLConnection connection = (HttpURLConnection) new URL(classUrl).openConnection();
        connection.setRequestMethod("GET");
        byte[] classBytes;
        try (InputStream in = connection.getInputStream()) {
            classBytes = in.readAllBytes();
        }

        // Load class dynamically
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> loaded = loader.defineClassFromBytes("automation.Test", classBytes);

        // Use reflection to call static method
        Method assertTrue = loaded.getMethod("display");
        assertTrue.invoke(loaded.getDeclaredConstructor().newInstance());

        System.out.println("Loaded automation.AssertionUtils and invoked assertTrue(true) successfully!");
    }
}