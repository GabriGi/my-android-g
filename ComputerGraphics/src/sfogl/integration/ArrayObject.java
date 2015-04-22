package sfogl.integration;



public class ArrayObject {

    private float[] verticesBuffer;
    private float[] normalsBuffer;
    private float[] txCoordsBuffer;
    private short[] indicesBuffer;

	private float minX, minY, minZ, maxX, maxY, maxZ;
	private float scaleX, scaleY, scaleZ, middleX, middleY, middleZ;
	
    
	public ArrayObject(float[] verticesBuffer, float[] normalsBuffer, float[] txCoordsBuffer, short[] indicesBuffer) {
		super();
		this.verticesBuffer = verticesBuffer;
		this.normalsBuffer = normalsBuffer;
		this.txCoordsBuffer = txCoordsBuffer;
		this.indicesBuffer = indicesBuffer;
		setupMinMaxScaleMiddle();
		normalizeVertices(this.verticesBuffer);
	}
	
    public float[] getNormalsBuffer() {
		return normalsBuffer;
	}
    
    public float[] getTxCoordsBuffer() {
		return txCoordsBuffer;
	}
    
    public float[] getVerticesBuffer() {
		return verticesBuffer;
	}
    
    public boolean isTxCoord(){
    	return txCoordsBuffer.length>0;
    }

    public short[] getIndicesBuffer() {
        return indicesBuffer;
    }
    
    public ArrayObject cloneWithTextureScaled(float sx,float sy,float sz){
    	float[] newtxCoords = txCoordsBuffer.clone();
    	sx*=2;	sy*=2;	sz*=2;

    	//Facce 1 e 2
    	newtxCoords[6] = newtxCoords[12] = newtxCoords[15] *= sx;
    	newtxCoords[24] = newtxCoords[30] = newtxCoords[33] *= sx;
    	newtxCoords[1] = newtxCoords[10] = newtxCoords[16] *= sy;
    	newtxCoords[19] = newtxCoords[28] = newtxCoords[34] *= sy;
    	//Facce Dx e Sx
    	newtxCoords[42] = newtxCoords[48] = newtxCoords[51] *= sz;
    	newtxCoords[60] = newtxCoords[66] = newtxCoords[69] *= sz;
    	newtxCoords[37] = newtxCoords[46] = newtxCoords[52] *= sy;
    	newtxCoords[55] = newtxCoords[64] = newtxCoords[70] *= sy;
    	//Facce A e B
    	newtxCoords[78] = newtxCoords[84] = newtxCoords[87] *= sx;
    	newtxCoords[96] = newtxCoords[102] = newtxCoords[105] *= sx;
    	newtxCoords[73] = newtxCoords[82] = newtxCoords[88] *= sz;
    	newtxCoords[91] = newtxCoords[100] = newtxCoords[106] *= sz;
    	
//    	Log.d("task", "New set of vertices");
//    	for (int i = 0; i < newtxCoords.length; i+=3) {
//    		if(i%18==0) Log.d("task", "-----------");
//    		Log.d("task", "vt "+newtxCoords[i]+" "+newtxCoords[i+1]+" "+newtxCoords[i+2]);
//		}
    	
    	ArrayObject a = new ArrayObject(verticesBuffer, normalsBuffer, newtxCoords, indicesBuffer);
    	a.setMinAndMaxValues(minX, minY, minZ, maxX, maxY, maxZ);
    	a.setScaleAndMiddleValues(scaleX, scaleY, scaleZ, middleX, middleY, middleZ);
    	return a;
    }
    
    public float[] getMinAndMaxValues() {
    	return new float[]{minX, minY, minZ, maxX, maxY, maxZ};
	}
    
    /**
     * @return in this order: scaleX, scaleY, scaleZ, middleX, middleY, middleZ
     */
    public float[] getScaleAndMiddleValues() {
    	return new float[]{1/scaleX, 1/scaleY, 1/scaleZ, middleX, middleY, middleZ};
	}

    private void setMinAndMaxValues(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
    
    private void setScaleAndMiddleValues(float scaleX, float scaleY, float scaleZ, float middleX, float middleY, float middleZ) {
    	this.scaleX = scaleX;
    	this.scaleY = scaleY;
    	this.scaleZ = scaleZ;
    	this.middleX = middleX;
    	this.middleY = middleY;
    	this.middleZ = middleZ;
    }
    
    /**
     * Deforma i vertici rendendoli di 2x2x2 (da -1 a 1)
     */    
    private float[] normalizeVertices(float[] vert) {
	    for (int i = 0; i < vert.length; i+=3) {
	    	vert[i]=scaleX*(vert[i]-middleX);
		}
	    for (int i = 1; i < vert.length; i+=3) {
	    	vert[i]=scaleY*(vert[i]-middleY);
		}
	    for (int i = 2; i < vert.length; i+=3) {
	    	vert[i]=scaleZ*(vert[i]-middleZ);
		}
	    return vert;
	}

	private void setupMinMaxScaleMiddle() {
		minX=maxX=verticesBuffer[0];
	    minY=maxY=verticesBuffer[1];
	    minZ=maxZ=verticesBuffer[2];
	    for (int i = 0; i < verticesBuffer.length; i+=3) {
//	    	Log.d("mesh", "x"+(i/3+1)+": "+vert[i]);
	    	if(verticesBuffer[i]>maxX) maxX=verticesBuffer[i];
	    	else if(verticesBuffer[i]<minX) minX=verticesBuffer[i];
		}
	    for (int i = 1; i < verticesBuffer.length; i+=3) {
//	    	Log.d("mesh", "y"+(i/3+1)+": "+vert[i]);
	    	if(verticesBuffer[i]>maxY) maxY=verticesBuffer[i];
	    	else if(verticesBuffer[i]<minY) minY=verticesBuffer[i];
		}
	    for (int i = 2; i < verticesBuffer.length; i+=3) {
//	    	Log.d("mesh", "z"+(i/3+1)+": "+vert[i]);
	    	if(verticesBuffer[i]>maxZ) maxZ=verticesBuffer[i];
	    	else if(verticesBuffer[i]<minZ) minZ=verticesBuffer[i];
		}
	    scaleX=2/(maxX-minX);
	    middleX=(minX+maxX)/2;
	    scaleY=2/(maxY-minY);
	    middleY=(minY+maxY)/2;
	    scaleZ=2/(maxZ-minZ);
	    middleZ=(minZ+maxZ)/2;
//	    Log.d("mesh", ""+minX+" "+minY+" "+minZ+" "+maxX+" "+maxY+" "+maxZ);
//	    Log.d("mesh", ""+1/scaleX+" "+1/scaleY+" "+1/scaleZ+" "+middleX+" "+middleY+" "+middleZ);
	}
    
}
