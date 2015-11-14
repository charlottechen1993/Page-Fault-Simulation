import java.util.ArrayList;
import java.util.HashMap;

public class Work {
	boolean disk_write = false;
	int hand = 0; 
	
	public Work(int hand){
		this.hand=hand;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int time_tick, int page_index, int tau){
		boolean found = false;
		int count = 0;
		
		while(count == cur_frames.size()){
			if(hand==cur_frames.size()){
				hand=0;
			}
			int cur = cur_frames.get(hand);
			//if r = 0; unreferenced
			if(page_table.get(cur).getRefBit()==0){
				int age = time_tick-page_table.get(cur).getTimeLastUsed();
				//if d=0; clean --> EVICT
				if(page_table.get(cur).getDirtyBit()==0){
					disk_write = false;
					page_table.remove(cur);
					cur_frames.remove(hand);
					cur_frames.add(hand, page_index);
					found = true;
				} 
				//if d = 1; dirty && age older than tau; out of working set --> write, mark clean, and continue searching
				else if(page_table.get(cur).getDirtyBit()==1 && age>tau){
					page_table.get(cur).setDirtyBit(0);
					page_table.get(cur).setTimeLastUsed(time_tick);
					disk_write = true;
					hand++;
				}
			}
			//if r=1; referenced --> continue searching
			else{
				//Page is in use now. Do not evict.
				hand++;
			}
			count++;
		}
		if(found == false){
			int oldest_time = page_table.get(cur_frames.get(0)).getTimeLastUsed();
			int oldest_page = cur_frames.get(0);
			int oldest_index = 0;
			
			for(int i=0; i<cur_frames.size(); i++){
				int cur = cur_frames.get(i);
				int cur_timestamp = page_table.get(cur).getTimeLastUsed();
				if(cur_timestamp<oldest_time){
					oldest_time=cur_timestamp;
					oldest_page = cur;
					oldest_index = i;
				}
			}
			if(page_table.get(oldest_page).getDirtyBit()==1){
				disk_write = true;
			}else
				disk_write = false;
			
			page_table.remove(oldest_page);
			cur_frames.remove(oldest_index);
			cur_frames.add(oldest_index, page_index);
		}
		hand++;
		
		return disk_write;
	}
	public int returnHand(){
		return hand;
	}
}
