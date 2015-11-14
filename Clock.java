import java.util.ArrayList;
import java.util.HashMap;

public class Clock {
	boolean dirty_evict = false;
	int hand = 0;
	public Clock(int hand){
		this.hand=hand;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int page_index, int time_tick){
		boolean found = false;
		
		while(found!=true && hand<=cur_frames.size()){
			//if head reach the end of the valid pages w/o evicting, start from beginning again

//			System.out.println("cur_frames size: " + cur_frames.size());
			if((int)hand==cur_frames.size()){
				hand=0;
//				System.exit(0);
			}
			//if r bit for current page is 0
			int cur = cur_frames.get(hand);
			
//			System.out.println(cur_frames );
//			System.out.println("curr frame : " + cur);
//			System.out.println("hand: " + hand);
			
//			System.out.println(hand + "    curr: " + cur);
//			System.out.println(page_table.get(cur));
			if(page_table.get(cur).getRefBit()==0){
				if(page_table.get(cur).getDirtyBit()==1){
					dirty_evict = true;
				}else
					dirty_evict = false;
				
				//evict page from page table and remove page from ordered list of pages
//				System.out.println("Replacing " + cur + " with " + page_index);
//				System.out.println();
				page_table.remove(cur);
				cur_frames.remove(hand);
				cur_frames.add(hand, page_index);
				found = true;
			} 
			//if r bit for current page is 1 set r bit back to 0
			else {
				page_table.get(cur).setRefBit(0);
				found = false;
				hand++;
			}
		}
		hand++;
		//if(dirty_evict==true)
			//System.out.println("page fault - evict dirty");
		//else
			//System.out.println("page fault - evict clean");
		
		return dirty_evict;
	}
	
	public int returnHand(){
		return hand;
	}
	
}
