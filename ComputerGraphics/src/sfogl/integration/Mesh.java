package sfogl.integration;

import sfogl2.SFOGLBufferObject;
import sfogl2.SFOGLShader;
import shadow.system.SFInitiable;
import android.opengl.GLES20;
import android.util.Log;


public class Mesh implements SFInitiable{

    private SFOGLBufferObject vertices=new SFOGLBufferObject();
    private SFOGLBufferObject normals=new SFOGLBufferObject();
    private SFOGLBufferObject indices=new SFOGLBufferObject();
    private SFOGLBufferObject txCoords=new SFOGLBufferObject();
    
	private float minX, minY, minZ, maxX, maxY, maxZ;
	private float scaleX, scaleY, scaleZ, middleX, middleY, middleZ;
	
    private ArrayObject arrayObject;
    //private int size;
    
	public Mesh() {
		super();
	}

    public boolean isTxCoord(){
    	return arrayObject.isTxCoord();
    }
    
	public Mesh(ArrayObject arrayObject) {
		super();
        this.arrayObject=arrayObject;
	}

    public void draw(SFOGLShader shader) {

        //Log.e("Mesh","Draw "+vertices.getBufferObject()+" "+normals.getBufferObject()+" ");
        shader.bindAttributef(ShadingProgram.VERTICES_INDEX, vertices.getBufferObject(), 3);/*position*/
        //Log.e("Mesh","Get Error A "+GLES20.glGetError());
        shader.bindAttributef(ShadingProgram.NORMALS_INDEX, normals.getBufferObject(), 3);/*normal*/
        //Log.e("Mesh","Get Error B "+GLES20.glGetError());
        if(isTxCoord())
            shader.bindAttributef(ShadingProgram.TXCOORD_INDEX, txCoords.getBufferObject(), 3);/*normal*/
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.getBufferObject());
        //Log.e("Mesh","Get Error C "+GLES20.glGetError());

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,arrayObject.getIndicesBuffer().length,GLES20.GL_UNSIGNED_SHORT,0);

        //short[] indices={0,1,2};



        //byteData.rewind();
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,3);//so you
        //Log.e("Mesh","Get Error D "+GLES20.glGetError());
    }
    
    public float[] getMinAndMaxValues() {
    	return new float[]{minX, minY, minZ, maxX, maxY, maxZ};
	}
    
    public float[] getScaleAndMiddleValues() {
    	return new float[]{1/scaleX, 1/scaleY, 1/scaleZ, middleX, middleY, middleZ};
	}
    
    private float[] normalizeVertices(float[] vert) {
    	minX=maxX=vert[0];
	    minY=maxY=vert[1];
	    minZ=maxZ=vert[2];
	    for (int i = 0; i < vert.length; i+=3) {
//	    	Log.d("mesh", "x"+(i/3+1)+": "+vert[i]);
	    	if(vert[i]>maxX) maxX=vert[i];
	    	else if(vert[i]<minX) minX=vert[i];
		}
	    for (int i = 1; i < vert.length; i+=3) {
//	    	Log.d("mesh", "y"+(i/3+1)+": "+vert[i]);
	    	if(vert[i]>maxY) maxY=vert[i];
	    	else if(vert[i]<minY) minY=vert[i];
		}
	    for (int i = 2; i < vert.length; i+=3) {
//	    	Log.d("mesh", "z"+(i/3+1)+": "+vert[i]);
	    	if(vert[i]>maxZ) maxZ=vert[i];
	    	else if(vert[i]<minZ) minZ=vert[i];
		}
	    scaleX=2/(maxX-minX);
	    middleX=(minX+maxX)/2;
	    for (int i = 0; i < vert.length; i+=3) {
	    	vert[i]=scaleX*(vert[i]-middleX);
		}
	    scaleY=2/(maxY-minY);
	    middleY=(minY+maxY)/2;
	    for (int i = 1; i < vert.length; i+=3) {
	    	vert[i]=scaleY*(vert[i]-middleY);
		}
	    scaleZ=2/(maxZ-minZ);
	    middleZ=(minZ+maxZ)/2;
	    for (int i = 2; i < vert.length; i+=3) {
	    	vert[i]=scaleZ*(vert[i]-middleZ);
		}
	    
//	    Log.d("mesh", ""+minX+" "+minY+" "+minZ+" "+maxX+" "+maxY+" "+maxZ);
//	    Log.d("mesh", ""+1/scaleX+" "+1/scaleY+" "+1/scaleZ+" "+middleX+" "+middleY+" "+middleZ);
	    return vert;
	}
    
	@Override
	public void init() {

        //Log.e("Mesh","Initialized");
        indices.loadData(arrayObject.getIndicesBuffer());

        //Log.e("Mesh","Get Error "+GLES20.glGetError());
        
        float[] vert = arrayObject.getVerticesBuffer().clone();
        vertices.loadData(normalizeVertices(vert));
        //vertices.loadData(arrayObject.getVerticesBuffer());
        normals.loadData(arrayObject.getNormalsBuffer());
        if(isTxCoord())
		     txCoords.loadData(arrayObject.getTxCoordsBuffer());

	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public SFOGLBufferObject getVertices() {
		return vertices;
	}

	public void setVertices(SFOGLBufferObject vertices) {
		this.vertices = vertices;
	}

	public SFOGLBufferObject getNormals() {
		return normals;
	}

	public void setNormals(SFOGLBufferObject normals) {
		this.normals = normals;
	}
}
