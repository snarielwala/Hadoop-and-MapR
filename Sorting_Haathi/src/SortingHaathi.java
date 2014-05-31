import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class SortingHaathi {

	 public static void main(String[] args) throws Exception {
		    if (args.length != 2) {
		      System.err.println("Usage: SkyDiver <input path> <output path>");
		      System.exit(-1);
		    }
		    
		    
		    Job job = new Job();
		    job.setJarByClass(SortingHaathi.class);
		    job.setJobName("SkyDiver");

		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    
		    job.setMapperClass(SortingMapper.class);
		    job.setReducerClass(SortingReducer.class);
		    
		    job.setMapOutputKeyClass(SortingKey.class);
		    job.setMapOutputValueClass(Text.class);

		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(Text.class);
		    
		    job.setPartitionerClass(NaturalKeyPartitioner.class) ;
		    job.setGroupingComparatorClass(SortingKeyGroupComparator.class) ;
		    job.setSortComparatorClass(CompositeKeyComparator.class) ;
		    
		    job.setNumReduceTasks(2);
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }

}
