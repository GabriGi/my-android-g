package sfogl.integration;

import java.util.ArrayList;

import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

public class Node {
	
	private ArrayList<Node> sonNodes;
	protected SFTransform3f relativeTransform;
	protected SFTransform3f effeciveTransform;
	private boolean enabled;
	private Model model;
	private boolean forceCoveredControl = false;
	
	public void setup(){
	    relativeTransform=new SFTransform3f();
	    effeciveTransform=new SFTransform3f();
	    sonNodes=new ArrayList<Node>();
	    model=null;
	    enabled=true;
	}
	
	public Node(){
	    setup();
	}
	
	/**
	 * Create a node with the specified model.
	 * Note that if a node has a model, he always will been covered tested.
	 * @param forceCoveredControl
	 */
	
	public Node(Model model){
	    setup();
	    this.model=model;
	}
	
	/**
	 * Create a node that force the covered control even if he has't specify a model.
	 * @param forceCoveredControl
	 */
	public Node(boolean forceCoveredControl){
	    setup();
	    this.forceCoveredControl = forceCoveredControl;
	}
	
	public Node(SFTransform3f relativeTransform){
	    setup();
	    this.relativeTransform=relativeTransform;
	}
	
	public Node(SFTransform3f relativeTransform,Model model){
	    setup();
	    this.relativeTransform=relativeTransform;
	    this.model=model;
	}
	
	public float getX(){
	    return this.effeciveTransform.getV()[9];
	}
	
	public float getY(){
	    return this.effeciveTransform.getV()[10];
	}
	
	public float getZ(){
	    return this.effeciveTransform.getV()[11];
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setEnabled(boolean enabled){
	    this.enabled=enabled;
	}
	
	public boolean isEnabled(){
	    return enabled;
	}
	
	public boolean isForceCoveredControl() {
		return forceCoveredControl;
	}
	
	public ArrayList<Node> getSonNodes() {
		return sonNodes;
	}
	
	public void setModel(Model model){
	    this.model=model;
	}
	
	public SFTransform3f getRelativeTransform() {
		return relativeTransform;
	}
	
	public Node clodeNode(){
        SFTransform3f transform=new SFTransform3f();
        transform.set(getRelativeTransform());
        Node node=new Node(transform,getModel());
        for (int i = 0; i < getSonNodes().size(); i++) {
            node.getSonNodes().add(getSonNodes().get(i).clodeNode());
        }
        return node;
    }
	
	public void updateTransform(SFTransform3f fatherTransform){
	  
        SFVertex3f position=new SFVertex3f();
        SFMatrix3f matrix=new SFMatrix3f();
        SFMatrix3f fatherMatrix=new SFMatrix3f();
        relativeTransform.getPosition(position);
        relativeTransform.getMatrix(matrix);
        fatherTransform.getMatrix(fatherMatrix);
        /* M' = Fm * M */
        matrix=fatherMatrix.MultMatrix(matrix);
        /* M' = Fm * Q + Fq */
        fatherTransform.transform(position);
        effeciveTransform.setPosition(position);
        effeciveTransform.setMatrix(matrix);
	}
	
	public void updateTree(SFTransform3f fatherTransform){
	    this.updateTransform(fatherTransform);
	    if(enabled){
	        for(int i=0;i<sonNodes.size();i++){
	            sonNodes.get(i).updateTree(effeciveTransform);
	        }
	    }
	}
	
	public void getOpenGLMatrix(float[] matrix){

        SFMatrix3f m=new SFMatrix3f();
        SFVertex3f vertex=new SFVertex3f();
        effeciveTransform.getPosition(vertex);
        effeciveTransform.getMatrix(m);

        matrix[0]=m.getA();
        matrix[1]=m.getD();
        matrix[2]=m.getG();
        matrix[3]=0;

        matrix[4]=m.getB();
        matrix[5]=m.getE();
        matrix[6]=m.getH();
        matrix[7]=0;

        matrix[8]=m.getC();
        matrix[9]=m.getF();
        matrix[10]=m.getI();
        matrix[11]=0;

        matrix[12]=vertex.getX();
        matrix[13]=vertex.getY();
        matrix[14]=vertex.getZ();
        matrix[15]=1;
    }
	
	public void draw(){
	    if(enabled){
	        if(model!=null){
	            float[] matrix=new float[16];
	            getOpenGLMatrix(matrix);
	            //glUniformMatrix4fv(shaderModel.getUniformTransform(), 1, false, matrix);
	            model.getProgram().setTransform(matrix);
	            model.draw();
	        }
	        for(int i=0;i<sonNodes.size();i++){
	            sonNodes.get(i).draw();
	        }
	    }
	}

	/**
	 * Check if the node is covered by anotherNode along all the three axis using a cubic geometry.
	 * The test will be done if the node specify a model or he is created with {@link #Node(boolean)}
	 * @param anotherNode
	 * @return true if the node is covered by anotherNode, false otherwise
	 */
	public boolean coveredBy(Node anotherNode){	//TODO non usare getV per la scala (se il nodo e' ruotato funziona male..)
		if(anotherNode.getModel()!=null || anotherNode.isForceCoveredControl()){
			float[] position = new float[3];
			position[0]= relativeTransform.getV()[9];
			position[1]= relativeTransform.getV()[10];
			position[2]= relativeTransform.getV()[11];
			float[] scale = new float[3];
			scale[0]= relativeTransform.getV()[0];
			scale[1]= relativeTransform.getV()[4];
			scale[2]= relativeTransform.getV()[8];
	
			float[] anotherPosition = new float[3];
			anotherPosition[0]= anotherNode.getRelativeTransform().getV()[9];
			anotherPosition[1]= anotherNode.getRelativeTransform().getV()[10];
			anotherPosition[2]= anotherNode.getRelativeTransform().getV()[11];
			float[] anotherScale = new float[3];
			anotherScale[0]= anotherNode.getRelativeTransform().getV()[0];
			anotherScale[1]= anotherNode.getRelativeTransform().getV()[4];
			anotherScale[2]= anotherNode.getRelativeTransform().getV()[8];
	
			boolean coveredX= !(((position[0]-scale[0])<=(anotherPosition[0]-anotherScale[0]) &&
								 (position[0]+scale[0])<=(anotherPosition[0]-anotherScale[0])) ||
								((position[0]-scale[0])>=(anotherPosition[0]+anotherScale[0]) &&
								 (position[0]+scale[0])>=(anotherPosition[0]+anotherScale[0])));
			boolean coveredY= !(((position[1]-scale[1])<=(anotherPosition[1]-anotherScale[1]) &&
								 (position[1]+scale[1])<=(anotherPosition[1]-anotherScale[1])) ||
								((position[1]-scale[1])>=(anotherPosition[1]+anotherScale[1]) &&
								 (position[1]+scale[1])>=(anotherPosition[1]+anotherScale[1])));
			boolean coveredZ= !(((position[2]-scale[2])<=(anotherPosition[2]-anotherScale[2]) &&
								 (position[2]+scale[2])<=(anotherPosition[2]-anotherScale[2])) ||
								((position[2]-scale[2])>=(anotherPosition[2]+anotherScale[2]) &&
								 (position[2]+scale[2])>=(anotherPosition[2]+anotherScale[2])));
			if(coveredX&&coveredY&&coveredZ){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * Check if the node is covered by anotherNode and all its son along all the three axis 
	 * using a cubic geometry (see {@link #coveredBy(Node)})
	 * @param anotherNode
	 * @return true if the node is covered by anotherNode, false otherwise
	 */
	public boolean coveredBySonNodes(Node anotherNode){
	    boolean covered = coveredBy(anotherNode);
	    if(covered) return true;
	    for(int i=0;i<anotherNode.getSonNodes().size();i++){
    		covered = coveredBySonNodes(anotherNode.getSonNodes().get(i));
    		if(covered) return true;
        }
		return false;
	}
	
	/**
	 * //Clear a node son list [and remove the node from memory] recursively.
	 */
	public void removeAllSonNodes() {
        for(int i=0;i<sonNodes.size();i++){
            sonNodes.get(i).removeAllSonNodes();
        }
        sonNodes.clear();	//Clear its son list...
        //Destroy();	//...and remove the node from memory.
	}
}




