import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;




public class SortingKeyGroupComparator extends WritableComparator {
			
			protected SortingKeyGroupComparator() {
				super(SortingKey.class,true) ;
			}
			
			
			@Override
			public int compare(WritableComparable k1, WritableComparable k2) {
				
				
				SortingKey ut1 = (SortingKey)k1 ;
				SortingKey ut2 = (SortingKey)k2 ;
				
				System.out.println("Grouping comp called for " + ut1 + ":" + ut2) ;
				
				
				int v =  ut1.getTicker().compareTo(ut2.getTicker()) ;
				
				return v ;
				
			}
			
		}
