package objLoader;

/**
 * Responsible for the +1/-1 issue on indices
 * 
 * @author Alessandro Martinelli
 */
public class ObjIndexSet implements Comparable<ObjIndexSet>{

	private int vIndex=-1;
	private int vnIndex=-1;
	private int vtIndex=-1;
	
	public ObjIndexSet(String indices){
		indices=indices.trim();
		int indexOfSlash01=indices.indexOf("/");
		int indexOfSlash02=indices.lastIndexOf("/");
		
		String[] splits=indices.split("/");

		vIndex=Integer.parseInt(splits[0])-1;
		if(indexOfSlash01>0){
			if(indexOfSlash01==indexOfSlash02){// v/vt
				vtIndex=Integer.parseInt(splits[1])-1;
			}else if(indexOfSlash01==indexOfSlash02+1){// v//vn
				vnIndex=Integer.parseInt(splits[1])-1;
			}else{// v/vt/vn
				vtIndex=Integer.parseInt(splits[1])-1;
				vnIndex=Integer.parseInt(splits[2])-1;
			}	
		}
	}
	
	public ObjIndexSet(int vIndex, int vtIndex, int vnIndex) {
		super();
		this.vIndex = vIndex;
		this.vnIndex = vnIndex;
		this.vtIndex = vtIndex;
	}
	
	public ObjIndexSet() {
	
	}
	
	public void set(ObjIndexSet set){
		this.vIndex=set.vIndex;
		this.vnIndex=set.vnIndex;
		this.vtIndex=set.vtIndex;
	}

	@Override
	public int compareTo(ObjIndexSet o) {
		int compare=vIndex-o.vIndex;
		if(compare==0){
			compare=vnIndex-o.vnIndex;
			if(compare==0){
				compare=vtIndex-o.vtIndex;
			}
		}
		return compare;
	}
	
	public void subtract(ObjIndexSet set){
		vIndex-=set.vIndex;
		vnIndex-=set.vnIndex;
		vtIndex-=set.vtIndex;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjIndexSet){
			return compareTo(((ObjIndexSet)obj))==0;
		}
		return super.equals(obj);
	}
	
	public int getvIndex() {
		return vIndex;
	}
	
	public int getVnIndex() {
		return vnIndex;
	}
	
	public int getVtIndex() {
		return vtIndex;
	}
	
	public void setvIndex(int vIndex) {
		this.vIndex = vIndex;
	}
	
	public void setVnIndex(int vnIndex) {
		this.vnIndex = vnIndex;
	}
	
	public void setVtIndex(int vtIndex) {
		this.vtIndex = vtIndex;
	}
	
	@Override
	public String toString() {
		if(vtIndex>0){
			if(vnIndex>0){
				return (vIndex+1)+"/"+(vtIndex+1)+"/"+(vnIndex+1);
			}else{
				return (vIndex+1)+"/"+(vtIndex+1);
			}
		}else if(vnIndex>0){
			return (vIndex+1)+"//"+(vnIndex+1);
		}else{
			return (vIndex+1)+"";
		}
	}
}
