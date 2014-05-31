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
		int indexofside=line.indexOf("|54=");
		int indexofticker=line.indexOf("|55=");
		int indexofprice=line.indexOf("|44=");
	
		
			if(indexofside!=-1&&indexofticker!=-1&&indexofprice!=-1)
			{	
				int endofticker=line.indexOf("|",indexofticker+4);
				String price = line.substring(indexofprice+4,line.indexOf("|",indexofprice+4));
				
					SortingKey skey = new SortingKey(line.substring(indexofside+4,indexofside+5),line.substring(indexofticker+4,endofticker),line.substring(0,15),price);   
					context.write(skey,new Text(""));
					
			}
		
  }
  
}