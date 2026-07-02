package mapper;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CacheMapper extends Mapper<Object, Text, Text, RatingStatsWritable> {

    private static final Logger LOG = Logger.getLogger(CacheMapper.class.getName());

    private static HashMap<String, String> movieGenreMap = new HashMap<>();

    private int processed = 0;
    private int skipped = 0;

    @Override
    protected void setup(Context context) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("movies.txt"));
        String line;

        while ((line = br.readLine()) != null) {

            line = line.trim();
            if (line.isEmpty()) continue;

            String[] fields = line.split(",");
            if (fields.length != 4) continue;
            String movieId = fields[0].trim().replaceAll("\\s+", "");
            String genre = fields[2].trim();

            movieGenreMap.put(movieId, genre);
        }

        br.close();
    }

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString().trim();

        if (line.isEmpty()) {
            skipped++;
            return;
        }

        String[] fields = line.split(",");

        if (fields.length != 4) {
            skipped++;
            return;
        }
        String movieId = fields[1].trim().replaceAll("\\s+", "");
        String ratingStr = fields[3].trim();

        int rating;

        try {
            rating = Integer.parseInt(ratingStr);
        } catch (Exception e) {
            skipped++;
            return;
        }

        if (rating < 1 || rating > 5) {
            skipped++;
            return;
        }
        String genre = movieGenreMap.get(movieId);

        if (genre == null) {
            genre = "UNKNOWN";
        }

        context.write(
                new Text(genre),
                new RatingStatsWritable(
                        rating,
                        1,
                        rating == 5 ? 1 : 0
                )
        );

        processed++;
    }
    @Override
    protected void cleanup(Context context) {

        System.out.println("TOTAL PROCESSED RECORDS: " + processed);
        System.out.println("TOTAL SKIPPED RECORDS: " + skipped);
    }
}

