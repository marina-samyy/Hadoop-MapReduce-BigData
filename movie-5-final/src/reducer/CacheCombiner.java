package reducer;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import mapper.RatingStatsWritable;

public class CacheCombiner extends Reducer<Text, RatingStatsWritable, Text, RatingStatsWritable> {

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

        context.write(
                key,
                new RatingStatsWritable(sum, count, fiveStar)
        );
    }
}

