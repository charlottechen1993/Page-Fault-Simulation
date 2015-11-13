
public class pageEntries {
	private int ref;
	private int dirty;
	private int timeLastUsed;
	
	public pageEntries(int ref, int dirty, int timeLastUsed){
		this.ref = ref;
		this.dirty = dirty;
		this.timeLastUsed = timeLastUsed;
	}
	public void setRefBit(int newRef){
		ref = newRef;
	}
	public void setDirtyBit(int newDirty){
		dirty = newDirty;
	}
	public void setTimeLastUsed(int newTimeLastUsed){
		timeLastUsed = newTimeLastUsed;
	}
	public int getRefBit(){
		return ref;
	}
	public int getDirtyBit(){
		return dirty;
	}
	public int getTimeLastUsed(){
		return timeLastUsed;
	}
	
}
