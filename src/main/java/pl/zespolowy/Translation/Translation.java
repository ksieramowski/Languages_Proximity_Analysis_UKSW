package pl.zespolowy.Translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.AppConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Getter
public class Translation {
    protected String[] source;
    protected String[] target;
    protected Map<String, String> translations;

    public Translation() {}

    public Translation(String[] source, String[] target) {
        boolean _ = Init(source, target);
    }


    public boolean Init(String[] source, String[] target) {
        this.source = source;
        this.target = target;

        if (source.length != target.length) {
            System.out.println("Invalid target length (source=" + source.length + ", target=" + target.length + ").");

            int max = source.length;
            if (target.length > max) { max = target.length; }
            for (int i = 0 ; i < max; i++) {
                String s = "";
                if (i < source.length) {
                    s = source[i];
                }
                String t = "";
                if (i < target.length) {
                    t = target[i];
                }

                System.out.println("[" + s + "] = " + t);
            }

            return false;
        }

        this.translations = new LinkedHashMap<>();

        for (int i = 0; i < source.length; i++) {
            this.translations.put(source[i], target[i]);
        }

        return true;
    }

    public boolean readCache(String languageName, String themeName) {

        File file = new File(AppConfig.ROOT_PATH + "/Cache/Translations/" + languageName + "/" + themeName + ".json");
        if (!file.exists()) {
            System.out.println("File doesn't exists.");
            return false;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            this.translations = objectMapper.readValue(file, Map.class);
            source = new String[this.translations.keySet().size()];
            target = new String[this.translations.keySet().size()];
            int i = 0;
            for (var key : translations.keySet()) {
                source[i] = key;
                target[i] = translations.get(key);
                i += 1;
            }

        } catch (IOException e) {
            System.out.println("TRANSLATION: FAILED TO READ CACHE FILE");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean writeCache(String languageName, String themeName) {
        ObjectMapper objectMapper = new ObjectMapper();

        File folder = new File(AppConfig.ROOT_PATH + "/Cache/Translations/" + languageName + "/");

        if (!folder.exists()) {
            boolean result = folder.mkdirs();
            if (!result) {
                return false;
            }
        }

        try {
            File file = new File(folder, themeName + ".json");
            if (file.exists()) {
                boolean _ = file.delete();
            }
            objectMapper.writeValue(file, this.translations);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public String sourceText() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append(key);
            if (i < translations.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    public String sourceText(String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append(key);
            if (i < translations.size() - 1) {
                sb.append(separator);
            }
            i++;
        }
        return sb.toString();
    }

    public String targetText() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append(value);
            if (i < translations.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    public String targetText(String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append(value);
            if (i < translations.size() - 1) {
                sb.append(separator);
            }
            i++;
        }
        return sb.toString();
    }

    public String translationsText() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append("[\"");
            sb.append(key);
            sb.append("\"] = \"");
            sb.append(value);
            sb.append("\"");
            if (i < translations.size() - 1) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    public String translationsText(String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append("[\"");
            sb.append(key);
            sb.append("\"] = \"");
            sb.append(value);
            sb.append("\"");
            if (i < translations.size() - 1) {
                sb.append(separator);
            }
            i++;
        }
        return sb.toString();
    }

    public String targetTextInCommas() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        for (var key : translations.keySet()) {
            String value = translations.get(key);
            sb.append("\"");
            sb.append(value);
            sb.append("\"");
            if (i < translations.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        sb.append("]");
        return sb.toString();
    }
}

