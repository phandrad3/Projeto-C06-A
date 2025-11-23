package com.example.lojabumi.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Obtém um valor Integer do Map
     * @param map O mapa contendo os dados
     * @param key A chave do valor desejado
     * @return O valor como Integer, ou null se não existir ou não for conversível
     */
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

    /**
     * Obtém um valor Double do Map
     * @param map O mapa contendo os dados
     * @param key A chave do valor desejado
     * @return O valor como Double, ou null se não existir ou não for conversível
     */
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

    /**
     * Obtém um valor String do Map
     * @param map O mapa contendo os dados
     * @param key A chave do valor desejado
     * @return O valor como String, ou null se não existir
     */
    public static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * Obtém um valor Boolean do Map
     * @param map O mapa contendo os dados
     * @param key A chave do valor desejado
     * @return O valor como Boolean, ou null se não existir ou não for conversível
     */
    public static Boolean getBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String strValue = value.toString().toLowerCase();
        if (strValue.equals("true")) {
            return true;
        } else if (strValue.equals("false")) {
            return false;
        }
        return null;
    }

    /**
     * Busca dados de uma tabela específica com ordenação e retorna como lista de Map
     * @param tableName Nome da tabela
     * @param orderBy Campo para ordenação (ex: "id")
     * @param ascending Se true, ordena ascendente; se false, descendente
     * @return Lista de Map, onde cada Map representa um objeto com chaves e valores
     */
    public static List<Map<String, Object>> getData(String tableName, String orderBy, boolean ascending) {
        try {
            // Constrói a URL com o parâmetro de ordenação
            String orderParam = ascending ? "order=" + orderBy + ".asc" : "order=" + orderBy + ".desc";
            URL endpoint = new URL(url + "/rest/v1/" + tableName + "?" + orderParam);

            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", key);
            connection.setRequestProperty("Authorization", "Bearer " + key);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Ler resposta
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                // Processar o JSON array para lista de Map
                return parseJsonArrayToMaps(response.toString());
            } else {
                System.out.println("❌ Falha ao recuperar dados. Código: " + responseCode);

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
                return new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("❌ Erro na recuperação de dados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca dados com filtros e ordenação e retorna como lista de Map
     * @param tableName Nome da tabela
     * @param filter Filtro no formato "campo=eq.valor"
     * @param orderBy Campo para ordenação (ex: "id")
     * @param ascending Se true, ordena ascendente; se false, descendente
     * @return Lista de Map, onde cada Map representa um objeto com chaves e valores
     */
    public static List<Map<String, Object>> getDataWithFilter(String tableName, String filter, String orderBy, boolean ascending) {
        try {
            // Constrói a URL com filtro e ordenação
            String orderParam = ascending ? "order=" + orderBy + ".asc" : "order=" + orderBy + ".desc";
            URL endpoint = new URL(url + "/rest/v1/" + tableName + "?" + filter + "&" + orderParam);

            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", key);
            connection.setRequestProperty("Authorization", "Bearer " + key);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Ler resposta
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                // Processar o JSON array para lista de Map
                return parseJsonArrayToMaps(response.toString());
            } else {
                System.out.println("❌ Falha ao recuperar dados filtrados. Código: " + responseCode);

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
                return new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("❌ Erro na recuperação de dados filtrados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Converte um array JSON em uma lista de Map<String, Object>
     * @param jsonArray String contendo o array JSON
     * @return Lista de Map
     */
    private static List<Map<String, Object>> parseJsonArrayToMaps(String jsonArray) {
        List<Map<String, Object>> result = new ArrayList<>();

        // Remove os colchetes do array
        String content = jsonArray.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1);
        }

        // Divide os objetos usando regex para lidar com objetos aninhados simples
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

    /**
     * Converte uma string JSON de objeto em um Map<String, Object>
     * @param jsonString String JSON de um objeto
     * @return Map com os pares chave-valor
     */
    private static Map<String, Object> parseJsonToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();

        // Remove as chaves externas
        String content = jsonString.trim();
        if (content.startsWith("{") && content.endsWith("}")) {
            content = content.substring(1, content.length() - 1);
        }

        // Divide em pares chave-valor
        Pattern pattern = Pattern.compile("\"([^\"]+)\":\\s*(\"[^\"]*\"|\\d+\\.?\\d*|true|false|null)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String key = matcher.group(1);
            String valueStr = matcher.group(2);

            // Converte o valor para o tipo apropriado
            Object value;
            if (valueStr.startsWith("\"") && valueStr.endsWith("\"")) {
                // String
                value = valueStr.substring(1, valueStr.length() - 1);
            } else if (valueStr.equals("true")) {
                value = true;
            } else if (valueStr.equals("false")) {
                value = false;
            } else if (valueStr.equals("null")) {
                value = null;
            } else {
                // Número - sempre converter para Double
                try {
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    value = valueStr; // Mantém como string se não conseguir converter
                }
            }

            map.put(key, value);
        }

        return map;
    }
}