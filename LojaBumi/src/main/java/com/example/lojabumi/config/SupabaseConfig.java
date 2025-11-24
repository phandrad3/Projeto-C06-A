package com.example.lojabumi.config;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupabaseConfig {
    private static final String SUPABASE_URL = "https://cfwsneatmmtsjpormtaa.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNmd3NuZWF0bW10c2pwb3JtdGFhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIyNTk1MTcsImV4cCI6MjA3NzgzNTUxN30.M77raYr4ZdmcSfmgeF3lJe1rb_QPD0gf9AtSXiJYUyc";

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            System.err.println("Erro ao gerar hash SHA-256: " + e.getMessage());
            return null;
        }
    }

    public static void insertData(String tableName, String jsonInputString) {
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

    public static String getUserByEmail(String email) {
        try {
            String emailEncoded = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
            String urlStr = SUPABASE_URL + "/rest/v1/usuario?select=*&email=eq." + emailEncoded;

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

    public static Integer getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static List<Map<String, Object>> getData(String tableName, String orderBy, boolean ascending) {
        try {
            String orderParam = ascending ? "order=" + orderBy + ".asc" : "order=" + orderBy + ".desc";
            URL endpoint = new URL(SUPABASE_URL + "/rest/v1/" + tableName + "?" + orderParam);

            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", SUPABASE_KEY);
            connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                return parseJsonArrayToMaps(response.toString());
            } else {
                System.out.println("❌ Falha ao recuperar dados. Código: " + responseCode);

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Resposta: " + response.toString());
                }
                return new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("❌ Erro na recuperação de dados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static List<Map<String, Object>> parseJsonArrayToMaps(String jsonArray) {
        List<Map<String, Object>> result = new ArrayList<>();

        String content = jsonArray.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1);
        }

        Pattern pattern = Pattern.compile("(\\{.*?\\})(?=,\\s*\\{|$)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String jsonStr = matcher.group(1);
            Map<String, Object> map = parseJsonToMap(jsonStr);
            if (map != null) {
                result.add(map);
            }
        }

        return result;
    }

    private static Map<String, Object> parseJsonToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();

        String content = jsonString.trim();
        if (content.startsWith("{") && content.endsWith("}")) {
            content = content.substring(1, content.length() - 1);
        }

        Pattern pattern = Pattern.compile("\"([^\"]+)\":\\s*(\"[^\"]*\"|\\d+\\.?\\d*|true|false|null)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String key = matcher.group(1);
            String valueStr = matcher.group(2);

            Object value;
            if (valueStr.startsWith("\"") && valueStr.endsWith("\"")) {
                value = valueStr.substring(1, valueStr.length() - 1);
            } else if (valueStr.equals("true")) {
                value = true;
            } else if (valueStr.equals("false")) {
                value = false;
            } else if (valueStr.equals("null")) {
                value = null;
            } else {
                try {
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    value = valueStr;
                }
            }

            map.put(key, value);
        }

        return map;
    }
}