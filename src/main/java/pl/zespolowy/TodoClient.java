package pl.zespolowy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TodoClient {

    private StringBuilder BASE_URL;
    private final HttpClient client;
    private String topic;

    public TodoClient(String topic) {
        this.client = HttpClient.newHttpClient();
        this.BASE_URL = new StringBuilder("https://api.conceptnet.io/related/c/en/___?filter=/c/en");
        this.setTopic(topic);
    }

    public void setTopic(String topic) {
        this.topic = topic;
        this.BASE_URL.replace(BASE_URL.indexOf("_"), BASE_URL.lastIndexOf("_") + 1, this.topic);
    }

    public Set<String> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.BASE_URL.toString()))
                .GET()
                .build();

        System.out.println(this.BASE_URL.toString());

        Set<String> wordSet = new HashSet<>();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        ObjectMapper mapper = new ObjectMapper();
        JsonNode mainBody = mapper.readTree(response.body());
        JsonNode related = mainBody.get("related");
        if (related != null && related.isArray()) {
            for (JsonNode item : related) {
                String id = item.get("@id").asText();
                String word = id.substring(id.lastIndexOf("/") + 1);
                wordSet.add(word);
            }
        }

        return wordSet;
    }
}