import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NRU {
	boolean disk_write = false;
	
	public NRU(){
	}
	
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int page_index){
		//keep 4 arraylists of all pages in frame based on eviction priorities
		ArrayList<Integer>priority1 = new ArrayList<Integer>();
		ArrayList<Integer>priority2 = new ArrayList<Integer>();
		ArrayList<Integer>priority3 = new ArrayList<Integer>();
		ArrayList<Integer>priority4 = new ArrayList<Integer>();
		
		//go through all pages in frame and allocate them to appropriate eviction list
		for(int pageIndex : cur_frames){
			if(page_table.get(pageIndex).getRefBit()==0 && page_table.get(pageIndex).getDirtyBit()==0){
				priority1.add(pageIndex);
			} else if (page_table.get(pageIndex).getRefBit()==0 && page_table.get(pageIndex).getDirtyBit()==1){
				priority2.add(pageIndex);
			} else if (page_table.get(pageIndex).getRefBit()==1 && page_table.get(pageIndex).getDirtyBit()==0){
				priority3.add(pageIndex);
			} else if (page_table.get(pageIndex).getRefBit()==1 && page_table.get(pageIndex).getDirtyBit()==1){
				priority4.add(pageIndex);
			} else {
				System.out.println("ERROR: priority list");
			}
		}
		
		Random rd = new Random ();
		
		//go through 4 groups of eviction group and randomly evict a page from lowest possible priority group
		int pageIndex = 0;
		if(!priority1.isEmpty()){
			pageIndex = priority1.get(rd.nextInt(priority1.size()));
		} 
		else if (!priority2.isEmpty()){
			pageIndex = priority2.get(rd.nextInt(priority2.size()));
			disk_write = true;
		} 
		else if (!priority3.isEmpty()){
			pageIndex = priority3.get(rd.nextInt(priority3.size()));
		} 
		else if (!priority4.isEmpty()){
			pageIndex = priority4.get(rd.nextInt(priority4.size()));
			disk_write = true;
		} else {
			System.out.println("ERROR: evict");
		}
		
		if(disk_write==true)
			System.out.println("Page fault - evict dirty");
		else
			System.out.println("Page fault - evict clean");
		System.out.println("Replace page " + pageIndex + " with page " + page_index + "\n");
		
		//remove victim page and replace with new page
		page_table.remove(pageIndex);
		int removeIndex = cur_frames.indexOf(pageIndex);
		cur_frames.remove(removeIndex);
		cur_frames.add(removeIndex, page_index);
		
		return disk_write;
	}
}
