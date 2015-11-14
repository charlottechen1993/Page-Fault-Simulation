import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class vmsim {
	//java vmsim –n <numframes> -a <opt|clock|nru|work> [-r <refresh>] [-t <tau>] <tracefile>
	int total_mem_access=0;
	String algorithm = null;
	int frame_num = 0;
	int r = -1; 				//optional
	int tau = -1; 				//optional
	String input_file = null;
	int page_faults = 0;
	int writes_to_disk = 0;
	int time_tick = 0;
	int tableCapacity = 0;
	int time_refreshed = 0;
	int hand = 0;
	HashMap<Integer, pageEntries>page_table = new HashMap<Integer, pageEntries>();
	ArrayList<Integer>cur_frames = new ArrayList<Integer>();
	HashMap<Integer, LinkedList<Integer>>optimal = new HashMap<Integer, LinkedList<Integer>>();
	
	
	public static void main(String [] args) throws IOException{
		new vmsim(args);
	}
	public vmsim(String[]args) throws IOException{
		parseCommandLineArgs(args);
		
		if(algorithm.equals("opt"))
			buildOptimalMap();
		
		// read file specified in user input
		File file = new File(input_file);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line;
		int refreshCnt = 0;
		while ((line = reader.readLine()) != null) {
			long page_index20 = 0;
			int page_index = 0;
			
	        String[]token = line.split(" ");
	        page_index20 = Long.parseLong(token[0], 16);
	        page_index = (int)(page_index20/Math.pow(2, 12));
	        
    		if(algorithm.equals("opt")){
        		if(optimal.get(page_index).peek()==time_tick)
        			optimal.get(page_index).pop();
        		else
        			System.out.println("ERROR: mismatch time and page index in optimal");
    		}
    		total_mem_access++;
	        time_tick++;
	        // === NO PAGE FAULT ===
	        if(page_table.containsKey(page_index)){
//	        	System.out.println(page_index + " is already in frame. [HIT]");
	        	// find entry value of the page and set reference bit to 1, and update dirty bit
	        	page_table.get(page_index).setRefBit(1);
	        	if(token[1].equals("W")){
	        		page_table.get(page_index).setDirtyBit(1);
	        	}
	        	
	        } 
	        // === PAGE FAULT ===
	        else{    
	        	page_faults++;
	        	// === PAGE FAULT if page frames NOT full ===
	        	if(page_table.size()<tableCapacity){
	        		//System.out.println(token[0] + " page fault - no eviction");
	        		//add page to the arraylist containing all valid pages
	        		//create a new page table value entry for the new page
	        		//swap page into the page table
	        		cur_frames.add(page_index);
	        		pageEntries page_entry = new pageEntries(time_tick);
	        		page_table.put(page_index, page_entry);
	        		if(token[1].equals("W")){
	        			page_table.get(page_index).setDirtyBit(1);
	        		}
	        	}
	        	//=== PAGE FAULT if page frames is full ===
	        	else if (page_table.size()==tableCapacity){
	        		boolean disk_write = false;
	        		
	        		//EVICT
	        		if(algorithm.equals("clock")){
	        			Clock replacement_c = new Clock(hand);
	        			disk_write = replacement_c.EvictPage(page_table, cur_frames, page_index);
	        			hand = replacement_c.returnHand();
	        		} else if(algorithm.equals("nru")){
	        			NRU replacement_n = new NRU();
	        			disk_write = replacement_n.EvictPage(page_table, cur_frames, page_index);
	        		} else if(algorithm.equals("work")){
	        			Work replacement_w = new Work(hand);
	        			disk_write = replacement_w.EvictPage(page_table, cur_frames, time_tick, page_index, tau);
	        			hand = replacement_w.returnHand();
	        		} else if(algorithm.equals("opt")){
	        			Opt replacement_o = new Opt();
	        			disk_write = replacement_o.EvictPage(page_table, cur_frames, optimal, time_tick, page_index);
	        		}
	        		//if evicted page is dirty, write_to_disk++
	        		if(disk_write == true){
	        			writes_to_disk++;
	        		}
	        		//create a new page table value entry for the new page
	        		//swap page into the page table
	        		pageEntries page_entry = new pageEntries(time_tick);
	        		page_table.put(page_index, page_entry);
	        		if(token[1].equals("W")){
	        			page_table.get(page_index).setDirtyBit(1);
	        		}
	        	} else {
	        		System.out.println("ERROR: Page entries exceeds page table capacity!");
	        	}
	        }
	        refreshCnt++;
	        // periodic refreshes
	        if((algorithm.equals("nru") || algorithm.equals("work")) && refreshCnt==r){
	        	for(pageEntries reset : page_table.values()){
	        		reset.setRefBit(0);
	        		if(algorithm.equals("work")){
	        			if(reset.getRefBit()==1)
	        				reset.setTimeLastUsed(time_tick);;
		        	}
	        	}
	        	refreshCnt=0;
	        	time_refreshed++;
	        }
	    }
		printResult();
	}
	public void buildOptimalMap() throws IOException{
		File file = new File(input_file);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int location = 0;
		String line;
		while ((line = reader.readLine()) != null) {
			long page_index20 = 0;
			int page_index = 0;
			
	        String[]token = line.split(" ");
	        page_index20 = Long.parseLong(token[0], 16);
	        page_index = (int)(page_index20/Math.pow(2, 12));
	        
	        //if page not in map yet
	        if(!optimal.containsKey(page_index)){
	        	LinkedList<Integer>page_locations = new LinkedList<Integer>();
	        	page_locations.add(location);
	        	optimal.put(page_index, page_locations);
	        } 
	        //if page already exist
	        else{
	        	optimal.get(page_index).add(location);
	        }
	        location++;
		}
	}
	
	public void printResult(){
		System.out.println("\n" + Character.toUpperCase(algorithm.charAt(0)) + algorithm.substring(1));
		System.out.println("Number of frames: " + frame_num);
		System.out.println("Total memory accesses: " + total_mem_access);
		System.out.println("Total page faults: " + page_faults);
		System.out.println("Total writes to disk: " + writes_to_disk + "\n");
		
		System.out.println("Total refreshes happened " + time_refreshed + "\n");
	}
	
	public void parseCommandLineArgs(String[]args){
		//counts the existence of required user inputs: n, a, and tracefile
		int required = 0; 
		
		int i = 0;
		String arg = null;
		//parse command line arguments
		while (i < args.length && args[i].startsWith("-")){
			arg = args[i++];
			
			//if find -n, parse out frame number
			if(arg.equals("-n")){
				required++;
				if(i<args.length){
					frame_num = Integer.parseInt(args[i++]);
					tableCapacity=frame_num;
				} 
			}
			
			//if find -a, parse out algorithm type
			if(arg.equals("-a")){
				required++;
				if(i<args.length){
					algorithm = args[i++];
					if(!algorithm.equals("clock")&&!algorithm.equals("nru")&&!algorithm.equals("opt")&&!algorithm.equals("work")){
						System.out.println("Please enter a valid algorithm type!");
	        			System.exit(1);
					}
				} 
			}
			//if find -r (optional), parse out r;
			//if no, r remains with default value -1
			if(arg.equals("-r")){
				if(i<args.length){
					r = Integer.parseInt(args[i++]);
				} 
			}
			
			//if find -t (optional), parse out t
			//if no, t remains with default value -1
			if(arg.equals("-t")){
				if(i<args.length){
					tau = Integer.parseInt(args[i++]);
				} 
			}
		}
		
		//if last cmd line argument contains file type "trace", parse out filename
		if(!args[args.length-1].contains("trace")){
			System.out.print("Please input a trace file!");
			System.exit(1);
		} else {
			required++;
			input_file = args[args.length-1];
		}
		
		/*
		 * if we counted that required input types is less than 3, it means user is 
		 * missing more than 1 arguments. 
		 */
		if(required != 3){
			System.out.println("Please make sure you entered n, a, and tracefile!");
			System.exit(1);
		}
	}
}
