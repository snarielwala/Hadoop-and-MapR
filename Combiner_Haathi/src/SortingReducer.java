//Author @Samarth - Rohan - Devaki - Steven




import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SortingReducer
  extends Reducer<SortingKey, Text, Text, Text> 
{
  
  @Override
  public void reduce(SortingKey key, Iterable<Text> values,Context context)
      throws IOException, InterruptedException
  {
     System.out.println("Reducer");
	  for(Text value:values)
	  {	
		  context.write(new Text("Ticker: "+key.getTicker()+" Time:"+key.getTime()+ " Side:"+key.getSide()+" "+"Price:"+key.getPrice()),value);
	  }	  
	
  }

}