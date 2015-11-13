import java.util.ArrayList;
import java.util.HashMap;

public class Clock {
	HashMap<Integer, pageEntries>page_table = new HashMap<Integer, pageEntries>();
	ArrayList<Integer>ordering = new ArrayList<Integer>();
	boolean dirty_evict = false;
	
	public Clock(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>ordering){
		this.page_table=page_table;
		this.ordering=ordering;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>ordering, int hand, int page_index){
		boolean found = false;
		
		while(found!=true && hand<=ordering.size()){
			//if head reach the end of the valid pages w/o evicting, start from beginning again
			if(hand==ordering.size()){
				hand=0;
			}
			//if r bit for current page is 0
			int cur = ordering.get(hand);
			if(page_table.get(cur).getRefBit()==0){
				if(page_table.get(cur).getDirtyBit()==1){
					dirty_evict = true;
				}else
					dirty_evict = false;
				
				//evict page from page table and remove page from ordered list of pages
				page_table.remove(cur);
				ordering.remove(hand);
				ordering.add(hand, page_index);
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
