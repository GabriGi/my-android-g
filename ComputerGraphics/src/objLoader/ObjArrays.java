package objLoader;

import java.util.ArrayList;

import shadow.math.SFVertex4f;

public class ObjArrays implements CommandLineInterpreter{
	
	public class ObjArraysPosition{
		public int vIndex;
		public int nIndex;
		public int txIndex;
		public int pIndex;
		public int fIndex;
		public ObjArraysPosition() {
			vIndex=vertices.size();
			nIndex=normals.size();
			txIndex=txCoords.size();
			pIndex=parameters.size();
			fIndex=faces.size();
		}
		
		@Override
		public String toString() {
			return "("+vIndex+","+nIndex+","+txIndex+","+","+pIndex+","+fIndex+")";
		}
	}

	ArrayList<SFVertex4f> vertices=new ArrayList<SFVertex4f>();
	ArrayList<SFVertex4f> normals=new ArrayList<SFVertex4f>();
	ArrayList<SFVertex4f> txCoords=new ArrayList<SFVertex4f>();
	ArrayList<SFVertex4f> parameters=new ArrayList<SFVertex4f>();
	ArrayList<ObjFace> faces=new ArrayList<ObjFace>();
	
	@Override
	public boolean takeCommandLine(String line) {

		if(line.startsWith("v ")){
			SFVertex4f value = readValue(line);
			vertices.add(value);
		}else if(line.startsWith("vp ")){
			SFVertex4f value = readValue(line);
			parameters.add(value);
		}else if(line.startsWith("vt ")){
			SFVertex4f value = readValue(line);
			txCoords.add(value);
		}else if(line.startsWith("vn ")){
			SFVertex4f value = readValue(line);
			normals.add(value);
		}else if(line.startsWith("f ")){
			ObjFace face = new ObjFace(line);
			faces.add(face);
		}else{
			return false;
		}
		
		return true;
	}
	
	private static SFVertex4f readValue(String line) {
		String[] elements = line.split(" ");
		SFVertex4f value = new SFVertex4f(0,0,0,1);
		for (int i = 1; i < elements.length; i++) {
			value.getV()[i-1]=Float.parseFloat(elements[i]);
		}
		return value;
	}
	
	public void traceArrays(){
		System.out.println("vertices "+vertices.size());
		System.out.println("normals "+normals.size());
		System.out.println("txCoords "+txCoords.size());
		System.out.println("parameters "+parameters.size());
		System.out.println("faces "+parameters.size());
	}
	
	public ObjArraysPosition generateObjArraysPosition(){
		return new ObjArraysPosition();
	}
	
	
	public ArrayList<SFVertex4f> getVertices() {
		return vertices;
	}
	
	public ArrayList<SFVertex4f> getParameters() {
		return parameters;
	}
	
	public ArrayList<SFVertex4f> getNormals() {
		return normals;
	}
	
	public ArrayList<ObjFace> getFaces() {
		return faces;
	}
	
	public ArrayList<SFVertex4f> getTxCoords() {
		return txCoords;
	}
}
