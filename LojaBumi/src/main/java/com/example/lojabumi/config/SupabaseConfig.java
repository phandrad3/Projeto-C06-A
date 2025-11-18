package com.example.lojabumi.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SupabaseConfig {

    static String url = "https://cfwsneatmmtsjpormtaa.supabase.co";
    static String key =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNmd3NuZWF0bW10c2pwb3JtdGFhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIyNTk1MTcsImV4cCI6MjA3NzgzNTUxN30.M77raYr4ZdmcSfmgeF3lJe1rb_QPD0gf9AtSXiJYUyc";

    // Teste 1: Verificar se o serviço está respondendo
    private static void testConnection(String url, String key) {
        try {
            URL endpoint = new URL(url + "/rest/v1/");
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", key);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Conexão com Supabase estabelecida com sucesso!");
            } else {
                System.out.println("❌ Falha na conexão. Código: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("❌ Erro de conexão: " + e.getMessage());
        }
    }

    // Teste 2: Inserir dados em uma tabela
    public static void testInsertData(String tableName, String jsonInputString) {
        try {
            URL endpoint = new URL(url + "/rest/v1/" + tableName);
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey", key);
            connection.setRequestProperty("Authorization", "Bearer " + key);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Prefer", "return=representation"); // Para retornar os dados inseridos
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201) {
                System.out.println("✅ Dados inseridos com sucesso!");

                // Ler resposta
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Resposta: " + response.toString());
                }
            } else {
                System.out.println("❌ Falha ao inserir dados. Código: " + responseCode);

                // Ler erro
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Resposta: " + response.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Erro na inserção: " + e.getMessage());
        }
    }
}