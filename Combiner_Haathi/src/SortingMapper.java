//Author @Samarth - Rohan - Devaki - Steven


import java.io.IOException;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SortingMapper
  extends Mapper<LongWritable, Text, SortingKey, Text> {

	
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException 
  {
        System.out.println("Mapper");
		String line = value.toString();
		int indexofside=line.indexOf("Side:");
		
		int indexofTime=line.indexOf("Time:");
		int indexofPrice=line.indexOf("Price:");
	
		SortingKey skey = new SortingKey(line.substring(indexofside+5,line.indexOf(" ", indexofside+1)),line.substring(8, line.indexOf(" ", 8)),line.substring(indexofTime+5,line.indexOf(" ", indexofTime+1)),line.substring(indexofPrice+6));   
		context.write(skey,new Text(""));
					
			
		
  }
  
}