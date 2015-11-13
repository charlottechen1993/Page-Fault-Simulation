import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class NRU {
	HashMap<Integer, pageEntries>page_table = new HashMap<Integer, pageEntries>();
	ArrayList<Integer>ordering = new ArrayList<Integer>();
	boolean dirty_evict = false;
	// Map that contains priority number as keys, and arraylist of corresponding pages as values
	HashMap<Integer, LinkedList<Integer>>priority = new HashMap<Integer, LinkedList<Integer>>();
	
	public NRU(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>ordering){
		this.page_table=page_table;
		this.ordering=ordering;
	}
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>ordering, int page_index){
		priority.put(1, new LinkedList<Integer>());	//first choice to evict
		priority.put(2, new LinkedList<Integer>());
		priority.put(3, new LinkedList<Integer>());
		priority.put(4, new LinkedList<Integer>());	//last choice to evict
		
		for(int pageIndex : ordering){
			if(page_table.get(pageIndex).getRefBit()==0 
					&& page_table.get(pageIndex).getDirtyBit()==0){
				priority.get(1).add(pageIndex);
			} else if (page_table.get(pageIndex).getRefBit()==0 
					&& page_table.get(pageIndex).getDirtyBit()==1){
				priority.get(2).add(pageIndex);
			} else if (page_table.get(pageIndex).getRefBit()==1 
					&& page_table.get(pageIndex).getDirtyBit()==0){
				priority.get(3).add(pageIndex);
			} else {
				priority.get(4).add(pageIndex);
			}
		}
		
		//if there exists page with eviction priority 1; first choice
		if(!priority.get(1).isEmpty()){
			EvictPriority(1, page_index);
		} 
		//if there exists page with eviction priority 2
		else if(!priority.get(2).isEmpty()){
			EvictPriority(2, page_index);
			dirty_evict = true;
		} 
		//if there exists page with eviction priority 3
		else if(!priority.get(3).isEmpty()){
			EvictPriority(3, page_index);
		} 
		//if there exists page with eviction priority 4; last choice
		else if(!priority.get(4).isEmpty()){
			EvictPriority(4, page_index);
			dirty_evict = true;
		} else{
			// Safety measure. Shouldn't happen
			System.out.println("ERROR: Trying to evict from empty list");
		}
//		System.out.print("arraylist size: " + ordering.size() + " ");
//		System.out.println("page table size: " + page_table.size());
		return dirty_evict;
	}
	
	public void EvictPriority(int priorityNum, int page_index){
		//get first element in the ordered list with eviction priority 1
		int pageIndex = priority.get(priorityNum).pop();
		//evict page from page table
		page_table.remove(pageIndex);
		//remove page from ordered list of pages
		ordering.remove(Integer.valueOf(pageIndex));
	}
}
