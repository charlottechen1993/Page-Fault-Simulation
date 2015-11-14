
public class pageEntries {
	private int ref;
	private int dirty;
	private int timeLastUsed;
	
	public pageEntries(int timeLastUsed){
		this.ref = 1;
		this.dirty = 0;
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
