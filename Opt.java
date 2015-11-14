import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Opt {
	boolean dirty_evict = false;
	
	public Opt(){
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, HashMap<Integer, LinkedList<Integer>>optimal, int time_tick, int page_index){
		int longestDistance=0; 
		int pageToEvict=0;
		//traverse through all current valid pages
		for(int i=0; i<cur_frames.size(); i++){
			int cur_page=cur_frames.get(i);
			
			//if find page that won't occur again, evict it
			if(optimal.get(cur_page).isEmpty()){
				pageToEvict = cur_frames.get(i);
				int dirty = page_table.get(pageToEvict).getDirtyBit();
				if(dirty ==1)
					dirty_evict=true;
				else if (dirty == 0)
					dirty_evict=false;
				else
					System.out.println("ERROR");
				
				page_table.remove(pageToEvict);
				cur_frames.remove(i);
				cur_frames.add(page_index);
				
				return dirty_evict;
			}
			
			int peek_location = optimal.get(cur_page).peekFirst();
			if(time_tick<=peek_location){
				if(peek_location>longestDistance){
					longestDistance=peek_location;
					pageToEvict=cur_page;
				}
			}
			else{
				System.out.println("ERROR: new page already exists in cur_frame");
			}
		}
		int dirty = page_table.get(pageToEvict).getDirtyBit();
		if(dirty ==1)
			dirty_evict=true;
		else if (dirty == 0)
			dirty_evict=false;
		else
			System.out.println("ERROR");
		
		page_table.remove(pageToEvict);
		cur_frames.remove(Integer.valueOf(pageToEvict));
		
		cur_frames.add(page_index);
		
		return dirty_evict;
	}
}
