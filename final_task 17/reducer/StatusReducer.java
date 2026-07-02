package reducer;
 
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
public class StatusReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
 
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
 
        int sum   = 0;
        int count = 0;

	for (IntWritable val : values) {
            sum += val.get();
            count++;
        }
 
        double average = (count > 0) ? (double) sum / count : 0.0;
 
        context.write(key, new IntWritable(sum));
 
        System.out.println("[StatusReducer] Status: " + key.toString()
                + " | Total: " + sum
                + " | Count: " + count
                + " | Avg: "   + String.format("%.2f", average));
    }
}
