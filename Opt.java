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
		int emptyCounts = 0;
		boolean foundEvict = false;
		//traverse through all current valid pages
		for(int i=0; i<cur_frames.size(); i++){
			int cur_page=cur_frames.get(i);
			int traverseLL = 0;
			boolean checked_longest = false;
			
			//if all valid pages won't ever occur again in the future, evict first page in all valid frames
			if(emptyCounts==cur_frames.size()-1){
				pageToEvict = cur_frames.get(0);
				int dirty = page_table.get(pageToEvict).getDirtyBit();
				if(dirty ==1)
					dirty_evict=true;
				else if (dirty == 0)
					dirty_evict=false;
				else
					System.out.println("ERROR");
				
				page_table.remove(pageToEvict);
				cur_frames.remove(0);
				cur_frames.add(page_index);
				return dirty_evict;
			}
			if(optimal.get(cur_page).isEmpty()){
				emptyCounts++;
				continue;
			}
			
			int linkedlistSize = optimal.get(cur_page).size();
			while(traverseLL < linkedlistSize && checked_longest==false){
				int peek_location = optimal.get(cur_page).peekFirst();
				if(time_tick>peek_location){
					optimal.get(cur_page).removeFirst();
				} else if(time_tick<=peek_location){
					if(peek_location>=longestDistance){
						longestDistance=peek_location;
						pageToEvict=cur_page;
						checked_longest=true;
						foundEvict=true;
					}
				}
				else{
					System.out.println("ERROR: new page already exists in cur_frame");
				}
				traverseLL++;
			}
		}
		//Again, if all valid pages won't ever occur again in the future, evict first page in all valid frames
		if(foundEvict==false){
			pageToEvict = cur_frames.get(0);
			int dirty = page_table.get(pageToEvict).getDirtyBit();
			if(dirty ==1)
				dirty_evict=true;
			else if (dirty == 0)
				dirty_evict=false;
			else
				System.out.println("ERROR");
			
			page_table.remove(pageToEvict);
			cur_frames.remove(0);
			cur_frames.add(page_index);
			
			return dirty_evict;
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
