import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.ObjectParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;


public class MyMavenProject {

    public static void httpRead() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://worldtimeapi.org/api/timezone/Europe/Bucharest.json"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ObjectParser mp = null;
        try {
            mp = mapper.readValue(response.body(), ObjectParser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ZonedDateTime localTime = ZonedDateTime.parse(mp.getUtc_datetime());
        ZonedDateTime timeCalculation = localTime.minusMinutes(150);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        int minute = localTime.getMinute();

        if (minute % 2 == 1) {
            System.out.println("The time is now " + localTime.format(formatter));
            System.out.println("Codrut has asked me to print t - 2h30m which is " + timeCalculation.format(formatter) + "\n");
        }
    }

    public static void timer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                httpRead();
            }
        }, 0, 60 * 1000);
    }
    public static void main(String[] args) {
        timer();
    }
}
