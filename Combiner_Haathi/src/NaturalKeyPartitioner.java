import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;




public class NaturalKeyPartitioner extends Partitioner<SortingKey,Text> {
	
	public void configure(JobConf job) {
		
		
	}
	
	@Override
	public int getPartition(SortingKey key, Text value, int numPartitions) {
		System.out.println(numPartitions);
		int hash = Math.abs(key.getTicker().hashCode()) ;
		int partition = hash % numPartitions ;

		
		return partition ;
		
	}

	
	

	

}
