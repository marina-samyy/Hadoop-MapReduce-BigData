package partitioner;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatusPartitioner extends Partitioner<Text, IntWritable> {

    @Override
    public int getPartition(Text key, IntWritable value, int numReduceTasks) {

        String status = key.toString();

        if (status.equals("Pending")) {
            return 0;
        } else if (status.equals("Completed")) {
            return 1;
        } else if (status.equals("Cancelled")) {
            return 2;
        } else {
            return 3;
        }
    }
}
