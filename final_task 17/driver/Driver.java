package driver;

import mapper.OrderMapper;
import reducer.StatusReducer;
import partitioner.StatusPartitioner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Driver {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Status Partitioning Job");

        job.setJarByClass(Driver.class);

        job.setMapperClass(OrderMapper.class);
        job.setReducerClass(StatusReducer.class);

        job.setPartitionerClass(StatusPartitioner.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setNumReduceTasks(4);

        if (args.length > 2 && args[2].equals("useCombiner")) {
            job.setCombinerClass(StatusReducer.class);
        }

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
