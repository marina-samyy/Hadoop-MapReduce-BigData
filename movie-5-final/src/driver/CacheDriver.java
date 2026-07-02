package driver;

import java.net.URI;

import mapper.CacheMapper;
import reducer.CacheReducer;
import reducer.CacheCombiner;
import mapper.RatingStatsWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CacheDriver {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: CacheDriver <input path> <output path> [useCombiner true|false]");
            System.exit(-1);
        }

        Configuration conf = new Configuration();

        boolean useCombiner = false;

        if (args.length == 3) {
            useCombiner = Boolean.parseBoolean(args[2]);
        }

        Job job = Job.getInstance(conf, "Movie Rating By Genre");

        job.setJarByClass(CacheDriver.class);
        job.setMapperClass(CacheMapper.class);
        job.setReducerClass(CacheReducer.class);
        job.setNumReduceTasks(1);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(RatingStatsWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        if (useCombiner) {
            job.setCombinerClass(CacheCombiner.class);
            System.out.println(">>> COMBINER ENABLED");
        } else {
            System.out.println(">>> COMBINER DISABLED");
        }
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.addCacheFile(new URI("hdfs:///user/cloudera/movies.txt"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

