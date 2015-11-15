import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NRU {
	boolean disk_write = false;
	
	public NRU(){
	}
	
	public boolean EvictPage(HashMap<Integer, pageEntries>page_table, ArrayList<Integer>cur_frames, int page_index){
		ArrayList<Integer>priority1 = new ArrayList<Integer>();
		ArrayList<Integer>priority2 = new ArrayList<Integer>();
		ArrayList<Integer>priority3 = new ArrayList<Integer>();
		ArrayList<Integer>priority4 = new ArrayList<Integer>();
		
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
		
		page_table.remove(pageIndex);
		int removeIndex = cur_frames.indexOf(pageIndex);
		cur_frames.remove(removeIndex);
		cur_frames.add(removeIndex, page_index);
		
		return disk_write;
	}
}
