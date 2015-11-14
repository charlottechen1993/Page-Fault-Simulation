import java.util.ArrayList;
import java.util.HashMap;

public class Clock {
	boolean dirty_evict = false;
	
	public Clock(){
		
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int hand, int page_index){
		boolean found = false;
		
		while(found!=true && hand<=cur_frames.size()){
			//if head reach the end of the valid pages w/o evicting, start from beginning again
			if(hand==cur_frames.size()){
				hand=0;
			}
			//if r bit for current page is 0
			int cur = cur_frames.get(hand);
			if(page_table.get(cur).getRefBit()==0){
				if(page_table.get(cur).getDirtyBit()==1){
					dirty_evict = true;
				}else
					dirty_evict = false;
				
				//evict page from page table and remove page from ordered list of pages
				page_table.remove(cur);
				cur_frames.remove(hand);
				cur_frames.add(hand, page_index);
				found = true;
			} 
			//if r bit for current page is 1 set r bit back to 0
			else {
				page_table.get(cur).setRefBit(0);
				found = false;
			}
			hand++;
		}
		
		//if(dirty_evict==true)
			//System.out.println("page fault - evict dirty");
		//else
			//System.out.println("page fault - evict clean");
		
		return dirty_evict;
	}
}
