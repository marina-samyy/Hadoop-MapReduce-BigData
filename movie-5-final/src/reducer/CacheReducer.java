package reducer;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import mapper.RatingStatsWritable;

public class CacheReducer extends Reducer<Text, RatingStatsWritable, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<RatingStatsWritable> values, Context context)
            throws IOException, InterruptedException {

        int sum = 0;
        int count = 0;
        int fiveStar = 0;

        for (RatingStatsWritable val : values) {
            sum += val.getSum();
            count += val.getCount();
            fiveStar += val.getFiveStar();
        }
        double avg = (count == 0) ? 0 : (double) sum / count;

        String output =
                count + "\t" +
                String.format("%.2f", avg) + "\t" +
                fiveStar;

        context.write(key, new Text(output));
    }
}

