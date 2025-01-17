package pl.zespolowy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Words.WordSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppConfig {
    @Getter
    @Setter
    public static class Config {
        boolean allowCache;
        boolean useCache;

        public Config() {
            allowCache = true;
            useCache = false;
        }

        public Config(boolean allowCache, boolean useCache) {
            this.allowCache = allowCache;
            this.useCache = useCache;
        }
    }

    public static final String ROOT_PATH = System.getProperty("user.dir");

    private static Config config = new Config();


    public static boolean getAllowCache() {
        return config.allowCache;
    }

    public static boolean getUseCache() {
        return config.useCache;
    }


    public static void read() {
        ObjectMapper objectMapper = new ObjectMapper();

        String path = ROOT_PATH + "/";
        String fileName = "appconfig.json";

        File file = new File(path + fileName);
        try {
            String content = Files.readString(Paths.get(path + fileName));
            AppConfig.config = objectMapper.readValue(content, AppConfig.Config.class);
        } catch (IOException e) {
            AppConfig.config.allowCache = true;
            AppConfig.config.useCache = false;

            write();
        }

        write();
    }

    public static void write() {
        String path = ROOT_PATH + "/";
        String fileName = "appconfig.json";
        File file = new File(path + fileName);
        try {
            new ObjectMapper().writeValue(file, AppConfig.config);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}
