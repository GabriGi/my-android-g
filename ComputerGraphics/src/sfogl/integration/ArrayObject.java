package sfogl.integration;


public class ArrayObject {

    private float[] verticesBuffer;
    private float[] normalsBuffer;
    private float[] txCoordsBuffer;
    private short[] indicesBuffer;
//    private short[] indicesOfTexturesBuffer;
    

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
	
//	public ArrayObject(float[] verticesBuffer, float[] normalsBuffer, float[] txCoordsBuffer, short[] indicesBuffer, short[] indicesOfTexturesBuffer) {
//		super();
//		this.verticesBuffer = verticesBuffer;
//		this.normalsBuffer = normalsBuffer;
//		this.txCoordsBuffer = txCoordsBuffer;
//		this.indicesBuffer = indicesBuffer;
//		this.indicesOfTexturesBuffer = indicesOfTexturesBuffer; 
//		setupMinMaxScaleMiddle();
//	}

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
    
//    public ArrayObject cloneScaled(float sx,float sy,float sz){
//    	float[] newvertices = verticesBuffer.clone();
//    	float[] newtxCoords = txCoordsBuffer.clone();
//    	newvertices = centerInZero(newvertices);
//    	//newtxCoords = rescaleVertices(newtxCoords);
////    	float sx=1/scaleX;
////    	float sy=1/scaleY;
////    	float sz=1/scaleZ;
//    	for (int i = 0; i < newtxCoords.length; i+=3) {
//			int j = 0;
//			while(indicesOfTexturesBuffer[j]!=(i/3)){
//				j++;
//			}
//			newvertices[(indicesBuffer[j])*3]*=sx;
//			newtxCoords[i]*=sx;
//			newvertices[(indicesBuffer[j])*3+1]*=sy;
//			newtxCoords[i]*=sy;
//			newvertices[(indicesBuffer[j])*3+2]*=sz;
//			newtxCoords[i]*=sz;
//		}
//    	
//    	Log.d("task", " ");
//    	for (int i = 0; i < newvertices.length; i+=3) {
//    		Log.d("task", "v " +newvertices[i]+" "+newvertices[i+1]+" "+newvertices[i+2]);
//		}
//    	for (int i = 0; i < newtxCoords.length; i+=3) {
//    		Log.d("task", "vt "+newtxCoords[i]+" "+newtxCoords[i+1]+" "+newtxCoords[i+2]);
//		}
//    	for (int i = 0; i < indicesBuffer.length; i++) {
//    		Log.d("task", "f "+(indicesBuffer[i]+1)+"/"+(indicesOfTexturesBuffer[i]+1));
//		}
//    	
//    	return new ArrayObject(newvertices, normalsBuffer, newtxCoords, indicesBuffer);
//    }
    
    public float[] getMinAndMaxValues() {
    	return new float[]{minX, minY, minZ, maxX, maxY, maxZ};
	}
    
    public float[] getScaleAndMiddleValues() {
    	return new float[]{1/scaleX, 1/scaleY, 1/scaleZ, middleX, middleY, middleZ};
	}

//    /**
//     * Trasla i vertici passati, di modo che il loro punto medio sia l'origine
//     */
//    private float[] centerInZero(float[] vert) {
//	    for (int i = 0; i < vert.length; i+=3) {
//	    	vert[i]=(vert[i]-middleX);
//		}
//	    for (int i = 1; i < vert.length; i+=3) {
//	    	vert[i]=(vert[i]-middleY);
//		}
//	    for (int i = 2; i < vert.length; i+=3) {
//	    	vert[i]=(vert[i]-middleZ);
//	    }
//	    return vert;
//	}
    
//    /**
//     * Riscala i vertici (texture) di modo che siano proporzionali ai vertici
//     */
//    private float[] rescaleVertices(float[] vert) {
//	    for (int i = 0; i < vert.length; i+=3) {
//	    	vert[i]=scaleX*vert[i];
//		}
//	    for (int i = 1; i < vert.length; i+=3) {
//	    	vert[i]=scaleY*vert[i];
//		}
//	    for (int i = 2; i < vert.length; i+=3) {
//	    	vert[i]=scaleZ*vert[i];
//		}
//	    return vert;
//	}
    
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
