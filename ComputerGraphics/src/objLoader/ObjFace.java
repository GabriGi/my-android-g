package objLoader;

public class ObjFace {

	private ObjIndexSet[] indices;

	public ObjFace(ObjIndexSet[] indices) {
		super();
		this.indices = indices;
	}
	
	public ObjFace(String fromString) {
		super();
		
		String[] splits=fromString.split(" ");
		indices=new ObjIndexSet[splits.length-1];
		
		for (int i = 0; i < splits.length-1; i++) {
			indices[i]=new ObjIndexSet(splits[i+1]);
		}
	}
	
	public ObjIndexSet[] getIndices() {
		return indices;
	}

}
