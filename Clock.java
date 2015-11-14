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
			//if hand reaches the end of the table w/o evicting, start from beginning
			if(hand==cur_frames.size()){
				hand=0;
			}
			//if r bit for current page is 0 --> EVICT
			int cur = cur_frames.get(hand);
			if(page_table.get(cur).getRefBit()==0){
				//check dirty bit of victim page
				if(page_table.get(cur).getDirtyBit()==1){
					disk_write = true;
				}else
					disk_write = false;
				
				if(disk_write==true)
					System.out.println("Page fault - evict dirty");
				else
					System.out.println("Page fault - evict clean");
				System.out.println("Replace " + cur + " with " + page_index + "\n");
				
				//remove victim page and replace with new page, break
				page_table.remove(cur);
				cur_frames.remove(hand);
				cur_frames.add(hand, page_index);
				found = true;
			} 
			//if r bit for current page is 1 --> set r bit back to 0 & advance clock
			else {
				page_table.get(cur).setRefBit(0);
				found = false;
				hand++;
			}
		}
		//advance clock pointer
		hand++;
		
		return disk_write;
	}
	
	//return new value of clock pointer
	public int returnHand(){
		return hand;
	}
}
