package com.example.lojabumi.config;

public class SupabaseConfig {
    private static final String SUPABASE_URL = "https://cfwsneatmmtsjpormtaa.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNmd3NuZWF0bW10c2pwb3JtdGFhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIyNTk1MTcsImV4cCI6MjA3NzgzNTUxN30.M77raYr4ZdmcSfmgeF3lJe1rb_QPD0gf9AtSXiJYUyc";

    public static String getSupabaseUrl() {
        return SUPABASE_URL;
    }

    public static String getSupabaseKey() {
        return SUPABASE_KEY;
    }
}