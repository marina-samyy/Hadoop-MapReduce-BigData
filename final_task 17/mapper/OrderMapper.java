package mapper;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class OrderMapper extends Mapper<Object, Text, Text, IntWritable> {

    private Text statusKey = new Text();
    private IntWritable amountValue = new IntWritable();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString().trim();

        String[] parts = line.split(",");
        if (parts.length != 5) {
            return;
        }

        try {
            String status = parts[1].trim();
            int amount = Integer.parseInt(parts[2].trim());
            if (amount <= 0) {
                return;
            }

            statusKey.set(status);
            amountValue.set(amount);

            context.write(statusKey, amountValue);

        } catch (NumberFormatException e) {
           
        }
    }
}
