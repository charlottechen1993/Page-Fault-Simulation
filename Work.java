import java.util.ArrayList;
import java.util.HashMap;

public class Work {
	boolean disk_write = false;
	int hand = 0;
	int disk_write_cnt = 0;
	
	public Work(int hand){
		this.hand=hand;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int time_tick, int page_index, int tau){
		boolean found = false;
		int saveHand = hand;
		int count = 0;
		
		//loop through all pages in frame only once
		while(saveHand != hand || count==0){
			count = 1;
			int cur = cur_frames.get(hand);
			int age = time_tick-page_table.get(cur).getTimeLastUsed();
			
			//if r = 0 and d = 0, EVICT directly and break
			if(page_table.get(cur).getRefBit()==0 && page_table.get(cur).getDirtyBit()==0){
				disk_write = false;
				page_table.remove(cur);
				cur_frames.remove(hand);
				cur_frames.add(hand, page_index);
				found = true;
				System.out.println("Page fault - evict clean");
				System.out.println("Replace page " + cur + " with page " + page_index + "\n");
				break;
			} 
			//if r=0, d = 1 && age older than tau; out of working set --> write, mark clean, and continue searching
			else if(page_table.get(cur).getRefBit()==0 && page_table.get(cur).getDirtyBit()==1 && age>tau){
				page_table.get(cur).setDirtyBit(0);
				disk_write_cnt++;
			}
			//if r = 1 or if r=0, d = 1, and age less than tau, continue searching
			//advance clock pointer
			if(hand==cur_frames.size()-1){
				hand=0;
			}else
				hand++;
		}
		//if loop through all pages without finding clean pages to evict, EVICT oldest page
		if(found == false){
			int oldest_time = page_table.get(cur_frames.get(0)).getTimeLastUsed();
			int oldest_page = cur_frames.get(0);
			int oldest_index = 0;
			
			//loop through all pages to find the oldest page based on time last used
			for(int i=0; i<cur_frames.size(); i++){
				int cur = cur_frames.get(i);
				int cur_timestamp = page_table.get(cur).getTimeLastUsed();
				if(cur_timestamp<oldest_time){
					oldest_time=cur_timestamp;
					oldest_page = cur;
					oldest_index = i;
				}
			}
			//advance hand to the oldest page to be evicted
			hand = cur_frames.indexOf(oldest_page);
			
			//check if victim page is dirty
			if(page_table.get(oldest_page).getDirtyBit()==1){
				disk_write = true;
			}else
				disk_write = false;
			
			if(disk_write==true)
				System.out.println("Page fault - evict dirty");
			else
				System.out.println("Page fault - evict clean");
			System.out.println("Replace page " + oldest_page + " with page " + page_index + "\n");
			
			//remove victim page and replace with new page
			page_table.remove(oldest_page);
			cur_frames.remove(oldest_index);
			cur_frames.add(oldest_index, page_index);
		}
		//advance hand
		if(hand==cur_frames.size()-1){
			hand=0;
		}else
			hand++;
		
		return disk_write;
	}
	
	public int returnHand(){
		return hand;
	}
	
	public int returnDiskWriteCount(){
		return disk_write_cnt;
	}
}
