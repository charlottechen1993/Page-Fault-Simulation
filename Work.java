import java.util.ArrayList;
import java.util.HashMap;

public class Work {
	boolean dirty_evict = false;
	int hand = 0; //index of hand in cur_frames
	
	public Work(){
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int hand, int time_tick, int page_index){
		
		
		return dirty_evict;
	}
	public int returnHand(){
		return hand;
	}
}
