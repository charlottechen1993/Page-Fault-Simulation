import java.util.ArrayList;
import java.util.HashMap;

public class Clock {
	boolean disk_write = false;
	int hand = 0;
	public Clock(int hand){
		this.hand=hand;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int page_index){
		boolean found = false;
		
		while(found!=true){
			//if head reach the end of the valid pages w/o evicting, start from beginning again

			if(hand==cur_frames.size()){
				hand=0;
			}
			//if r bit for current page is 0
			int cur = cur_frames.get(hand);
			if(page_table.get(cur).getRefBit()==0){
				if(page_table.get(cur).getDirtyBit()==1){
					disk_write = true;
				}else
					disk_write = false;
				
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
		//if(disk_write==true)
			//System.out.println("page fault - evict dirty");
		//else
			//System.out.println("page fault - evict clean");
		
		return disk_write;
	}
	
	public int returnHand(){
		return hand;
	}
	
}
