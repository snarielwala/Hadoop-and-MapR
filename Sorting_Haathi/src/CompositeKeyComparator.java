import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CompositeKeyComparator extends WritableComparator {
			
			protected CompositeKeyComparator() {
				super(SortingKey.class,true) ;
			}
			
			public int compare(WritableComparable k1, WritableComparable k2) {
				
				SortingKey ut1 = (SortingKey)k1 ;
				SortingKey ut2 = (SortingKey)k2 ;
				
				return ut1.compareTo(ut2) ;
			}
		}
		