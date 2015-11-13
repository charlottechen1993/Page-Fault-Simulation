import java.util.ArrayList;
import java.util.HashMap;

public class Work {
	HashMap<Integer, pageEntries>page_table = new HashMap<Integer, pageEntries>();
	ArrayList<Integer>ordering = new ArrayList<Integer>();
	boolean dirty_evict = false;
	int hand = 0; //index of hand in ordering
	
	public Work(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>ordering){
		this.page_table=page_table;
		this.ordering=ordering;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>ordering, int hand){
		
		
		return dirty_evict;
	}
	public int returnHand(){
		
		return hand;
	}
}
