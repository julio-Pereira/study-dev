package org.study.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestExercise {
    private static final Logger logger = LoggerFactory.getLogger(RestExercise.class);

    public static void main(String[] args) throws IOException {
        findCountries("bra", "americas");
        findCountries("por", "europe");
        findCountries("ind", "asia");
        findCountries("aus", "oceania");
        findCountries("can", "americas");
    }

    public static List<String> findCountries(String keyword, String region) throws IOException {
        List<String> countries = new ArrayList<>();
        String uri = String.format("https://jsonmock.hackerrank.com/api/countries/search?name=%s&region=%s&page=%s", keyword, region, String.valueOf(1));
        String country = "";
        String population = "";
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        int status = con.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK) {
            logger.info("Connection established successfully");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(content.toString());

            country = node.get("data").get(0).get("name").asText();
            population = node.get("data").get(0).get("population").asText();
            logger.info("Country: {}, Population: {}", country, population);
            countries.add(country + "," + population);
            in.close();
        }

        con.disconnect();
        logger.info("Connection closed");

        return countries;
    }
}