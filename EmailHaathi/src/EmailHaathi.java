//Author @ Samarth - Rohan - Devaki
//The main class 
//Set the mapper and reducer classes 


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class EmailHaathi {

	 public static void main(String[] args) throws Exception {
		    if (args.length != 2) {
		      System.err.println("Usage: SkyDiver <input path> <output path>");
		      System.exit(-1);
		    }
		    Configuration conf = new Configuration(true);
		    //conf.set("textinputformat.record.delimiter","Message-ID:");
		    
		    Job job = new Job(conf);
		    job.setInputFormatClass(EmailInputFormat.class);
		    job.setJarByClass(EmailHaathi.class);
		    job.setJobName("EmailHaathi");
		   
		    
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    
		    job.setMapperClass(EmailMapper.class);
		    job.setReducerClass(EmailReducer.class);
		  
		    job.setMapOutputKeyClass(EmailKey.class);
		    job.setMapOutputValueClass(Text.class);

		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(Text.class);
		    
//		    job.setPartitionerClass(NaturalKeyPartitioner.class) ;
//		    job.setGroupingComparatorClass(SortingKeyGroupComparator.class) ;
//		    job.setSortComparatorClass(CompositeKeyComparator.class) ;
		    
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }

}
