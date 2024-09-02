import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherFunctions {

    public static HttpRequest createRequest(String url) {
        //Create HTTP Request with url
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    public static HttpResponse<String> sendRequest(HttpRequest request) {
        //Send HTTP Request with url and collect response
        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Request failed. Please check your" +
                    " internet connection.", "Error sending request", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // required to compile ?
        }
    }
}
