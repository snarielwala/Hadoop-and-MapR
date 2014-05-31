
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class EmailInputFormat extends FileInputFormat<LongWritable, Text> {

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		System.out.println(split.toString());
		context.setStatus(split.toString());
		return new EmailRecordReader();
	}

	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException {
		System.out.println("getSplits");
		List<InputSplit> splits = new ArrayList<>();
		// repeat for all the input files
		for (FileStatus status : listStatus(job)) {
			splits.addAll(getSplitsForMail(job));
	    }
	    return splits;
	}
	
		private static final double SPLIT_SLOP = 1.1;   // 10% slop
	  private static final Log LOG = LogFactory.getLog(FileInputFormat.class);
	  static final String NUM_INPUT_FILES = "mapreduce.input.num.files";

	  /** 
	   * Generate the list of files and make them into FileSplits.
	   */ 
	  public List<InputSplit> getSplitsForMail(JobContext job
	                                    ) throws IOException {
	    long minSize = Math.max(getFormatMinSplitSize(), getMinSplitSize(job));
	    long maxSize = getMaxSplitSize(job);

	    // generate splits
	    List<InputSplit> splits = new ArrayList<InputSplit>();
	    List<FileStatus>files = listStatus(job);
	    
	    // loop over all files
	    for (FileStatus file: files) {
	      Path path = file.getPath();
	      FileSystem fs = path.getFileSystem(job.getConfiguration());
	      long length = file.getLen();
	      BlockLocation[] blkLocations = fs.getFileBlockLocations(file, 0, length);
	      
	      
	      if ((length != 0) && isSplitable(job, path)) { 
	        long blockSize = file.getBlockSize();
	        long splitSize = computeSplitSize(blockSize, minSize, maxSize);

	        long bytesRemaining = length;
	        while (((double) bytesRemaining)/splitSize > SPLIT_SLOP) {
	          int blkIndex = getBlockIndex(blkLocations, length-bytesRemaining);
	          splits.add(new FileSplit(path, length-bytesRemaining, splitSize, 
	                                   blkLocations[blkIndex].getHosts()));
	          bytesRemaining -= splitSize;
	        }
	        
	        // last data chunk
	        if (bytesRemaining != 0) {
	          splits.add(new FileSplit(path, length-bytesRemaining, bytesRemaining, 
	                     blkLocations[blkLocations.length-1].getHosts()));
	        }
	        
	      // not splitable
	      } else if (length != 0) {
	        splits.add(new FileSplit(path, 0, length, blkLocations[0].getHosts()));
	      } else { 
	        //Create empty hosts array for zero length files
	        splits.add(new FileSplit(path, 0, length, new String[0]));
	      }
	    }
	    
	    // Save the number of input files in the job-conf
	    job.getConfiguration().setLong(NUM_INPUT_FILES, files.size());

	    LOG.debug("Total # of splits: " + splits.size());
	    
	    return splits;
	  }
	
}
