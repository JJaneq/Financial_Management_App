package com.edp.projekt.external_api;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FinancialApi {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./src/")
            .ignoreIfMissing()
            .ignoreIfMalformed()
            .load();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String baseUrl = "https://stooq.pl/q/d/l/";
    private static final String apiKey = dotenv.get("ALPHA_VANTAGE_API_KEY");

    public static String getFinancialData(String symbol, char interval) throws IOException, InterruptedException {
        StringBuilder endpoint = new StringBuilder(baseUrl);
        endpoint.append("?s=").append(symbol);
        endpoint.append("&i=").append(interval);

        try {
            URL url = new URL(endpoint.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

            rd.close();
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return endpoint.toString();
    }
}
