package com.example.lojabumi.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.OutputStreamWriter;

public class SupabaseConfig {
    private static final String SUPABASE_URL = "https://cfwsneatmmtsjpormtaa.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNmd3NuZWF0bW10c2pwb3JtdGFhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIyNTk1MTcsImV4cCI6MjA3NzgzNTUxN30.M77raYr4ZdmcSfmgeF3lJe1rb_QPD0gf9AtSXiJYUyc";

    public static void testInsertData(String tableName, String jsonInputString) {
        try {
            URL url = new URL(SUPABASE_URL + "/rest/v1/" + tableName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Prefer", "return=representation");

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(jsonInputString);
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();
            if (responseCode != 201) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Falha ao inserir dados. Código: " + responseCode);
                System.out.println("Resposta: " + response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String testSelectAllData(String tableName) {
        try {
            String urlStr = SUPABASE_URL + "/rest/v1/" + tableName + "?select=*";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}