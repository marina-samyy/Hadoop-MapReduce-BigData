package utils;

import java.io.*;
import java.util.HashMap;

public class MovieCacheLoader {

    public static HashMap<String, String> loadMovies(String filePath) {

        HashMap<String, String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length != 4) continue;
                String movieId = parts[0].trim();
                String genre   = parts[2].trim();

                if (!movieId.isEmpty() && !genre.isEmpty()) {
                    map.put(movieId, genre);
                }
            }

            br.close();

        } catch (Exception e) {
            System.err.println("Error loading cache file: " + e.getMessage());
        }
        return map;
    }
}

