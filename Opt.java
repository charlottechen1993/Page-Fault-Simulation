import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Opt {
	boolean disk_write = false;
	
	public Opt(){
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, HashMap<Integer, LinkedList<Integer>>optimal, int time_tick, int page_index){
		int longestDistance=0; //store farthest distance
		int pageToEvict=0;	//store page that won't be used until farthest in future
		
		//traverse through all valid pages
		for(int i=0; i<cur_frames.size(); i++){
			int cur_page=cur_frames.get(i);
			
			//if find a page that won't occur again --> directly EVICT and return
			if(optimal.get(cur_page).isEmpty()){
				pageToEvict = cur_frames.get(i);
				
				//check dirty bit of victim page
				int dirty = page_table.get(pageToEvict).getDirtyBit();
				if(dirty ==1)
					disk_write=true;
				else if (dirty == 0)
					disk_write=false;
				
				//remove victim page and replace with new page, return
				page_table.remove(pageToEvict);
				cur_frames.remove(i);
				cur_frames.add(page_index);
				return disk_write;
			}
			//look at next occurrence of current page
			int peek_location = optimal.get(cur_page).peekFirst();
			if(time_tick<=peek_location){
				//if next occurrence of current page is further than saved farthest distance, update farthest distance and pageToEvict
				if(peek_location>longestDistance){
					longestDistance=peek_location;
					pageToEvict=cur_page;
				}
			}
			else{
				System.out.println("ERROR: new page already exists in cur_frame");
			}
		}
		//check dirty bit of victim page
		int dirty = page_table.get(pageToEvict).getDirtyBit();
		if(dirty ==1)
			disk_write=true;
		else if (dirty == 0)
			disk_write=false;
		
		//remove victim page and replace with new page
		page_table.remove(pageToEvict);
		cur_frames.remove(Integer.valueOf(pageToEvict));
		cur_frames.add(page_index);
		
		return disk_write;
	}
}
