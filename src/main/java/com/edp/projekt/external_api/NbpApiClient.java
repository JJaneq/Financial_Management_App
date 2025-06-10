package com.edp.projekt.external_api;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NbpApiClient {

    private static final String API_URL = "https://api.nbp.pl/api/exchangerates/rates/a/";
    private String baseCurrency;

    public NbpApiClient(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    private JSONObject getCurrencyRates() throws Exception {
        String urlString = API_URL + baseCurrency + "/?format=json";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Błąd podczas pobierania danych z API NBP. Kod odpowiedzi: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse;
    }

    public double getExchangeRate() {
        if (baseCurrency.equals("PLN")) {return 1.0;};
        JSONObject ratesData = null;
        try {
            ratesData = getCurrencyRates();
        } catch (Exception e) {
            Logger.getLogger(NbpApiClient.class.getName()).log(Level.SEVERE, "Error in getExchangeRate: " + e.getMessage());
            return -1;
        }

        JSONArray rates = ratesData.getJSONArray("rates");
        JSONObject rate = rates.getJSONObject(0);
        return rate.getDouble("mid");
    }
}
