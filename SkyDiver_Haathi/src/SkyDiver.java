
// @Authors@ Samarth - Rohan - Devaki - Steven 
// This class SkyDiver contains the main class 
//This class creates a job and sets the mapper and reducer classes 

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SkyDiver {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: SkyDiver <input path> <output path>");
      System.exit(-1);
    }
    
    
    Job job = new Job();
    job.setJarByClass(SkyDiver.class);
    job.setJobName("SkyDiver");

    FileInputFormat.addInputPath(job, new Path(args[0]));	//command line arguements for the input and output files 
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    job.setMapperClass(SkyDiverMapper.class); //setting mapper class 
    job.setReducerClass(SkyDiverReducer.class); //setting reducer class 
    
    job.setMapOutputKeyClass(SkyDiverKey.class); 	//set output of the mapper (key,value)
    job.setMapOutputValueClass(SkyDiverValue.class);

    job.setOutputKeyClass(Text.class);	//set output of the reducer 
    job.setOutputValueClass(Text.class);
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}