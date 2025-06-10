//package com.edp.projekt.external_api;
//
//import io.github.cdimascio.dotenv.Dotenv;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.URL;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//public class FinancialApi {
//    private static final Dotenv dotenv = Dotenv.configure()
//            .directory("./src/")
//            .ignoreIfMissing()
//            .ignoreIfMalformed()
//            .load();
//    private static final HttpClient httpClient = HttpClient.newHttpClient();
//    private static final String baseUrl = "https://stooq.pl/q/d/l/";
//    private static final String apiKey = dotenv.get("ALPHA_VANTAGE_API_KEY");
//
//    public static String getFinancialData(String symbol, char interval) throws IOException, InterruptedException {
//        StringBuilder endpoint = new StringBuilder(baseUrl);
//        endpoint.append("?s=").append(symbol);
//        endpoint.append("&i=").append(interval);
//
//        try {
//            URL url = new URL(endpoint.toString());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            rd.close();
//            conn.disconnect();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return endpoint.toString();
//    }
//}
//package com.edp.projekt.external_api;
//
//import com.edp.projekt.db.Stock;
//import io.github.cdimascio.dotenv.Dotenv;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//public class FinancialApi {
//    private static final Dotenv dotenv = Dotenv.configure()
//            .directory("./src/")
//            .ignoreIfMissing()
//            .ignoreIfMalformed()
//            .load();
//
//    private static final HttpClient httpClient = HttpClient.newHttpClient();
//    private static final String apiKey = dotenv.get("ALPHA_VANTAGE_API_KEY");
//    private static final String baseUrl = "https://www.alphavantage.co/query";
//
//    public static String getFinancialData(String symbol, String interval) throws IOException, InterruptedException {
//        // Example for TIME_SERIES_INTRADAY
//        String url = String.format(
//                "%s?function=TIME_SERIES_INTRADAY&symbol=%s&interval=%s&outputsize=full&apikey=%s",
//                baseUrl, symbol, interval, apiKey
//        );
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .GET()
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        // For now, just print raw JSON
//        System.out.println(response.body());
//
//        return response.body();
//    }
//
//    public static Stock getStockData(String symbol) throws IOException, InterruptedException {
//        String url = String.format(
//                "%s?function=SYMBOL_SEARCH&keywords=%s&apikey=%s",
//                baseUrl, symbol, apiKey
//        );
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .GET()
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        JSONObject jsonResponse = new JSONObject(response.body());
//        JSONArray matches = jsonResponse.getJSONArray("bestMatches");
//
//        if (!matches.isEmpty()) {
//            JSONObject firstMatch = matches.getJSONObject(0);
//            return new Stock(firstMatch.getString("2. name"), symbol);
//        } else {
////            return "Nie znaleziono firmy dla symbolu: " + symbol;
//            return null;
//        }
//    }
//
//}

package com.edp.projekt.external_api;

import com.edp.projekt.db.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

public class FinancialApi {

    public static String getFinancialData(String symbol) throws IOException {
        // Stooq: np. https://stooq.pl/q/d/l/?s=aapl.us&i=d
        String url = "https://stooq.pl/q/d/l/?s=" + symbol.toLowerCase() + "&i=d";

        StringBuilder csvData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                csvData.append(line).append("\n");
            }
        }
        System.out.println(csvData.toString());
        return csvData.toString();
    }

    public static boolean isStockExist(String symbol) throws IOException {
        String url = "https://stooq.pl/q/d/l/?s=" + symbol.toLowerCase() + "&i=d";
        StringBuilder csvData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line = reader.readLine();
            if (line.equals("Brak danych\n") || line.equals("Brak danych"))
                return false;
        }
        return true;
    }
}
