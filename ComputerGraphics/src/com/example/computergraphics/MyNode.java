package com.example.computergraphics;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

public class MyNode extends Node {
	
	private boolean forceCoveredControl = false;
	private float scaleX, scaleY, scaleZ;
	private float posX, posY, posZ;
	
	public MyNode() {
		super();
	}
	
	public MyNode(SFTransform3f relativeTransform) {
		super(relativeTransform);
	}
	
	public MyNode(SFTransform3f relativeTransform, Model model) {
		super(relativeTransform, model);
	}

	/**
	 * Create a node with the specified model.
	 * Note that if a node has a model, he always will been covered tested.
	 * @param forceCoveredControl
	 */
	
	public MyNode(Model model){
	    super();
	    setModel(model);
	}

	/**
	 * Create a node that force the covered control even if he has't specify a model.
	 * @param forceCoveredControl
	 */
	public MyNode(boolean forceCoveredControl){
	    super();
	    this.forceCoveredControl = forceCoveredControl;
	}
	
	public boolean isForceCoveredControl() {
		return forceCoveredControl;
	}

	public void setScale(float scaleX, float scaleY, float scaleZ){
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		getRelativeTransform().setMatrix(SFMatrix3f.getScale(scaleX, scaleY, scaleZ));
	}
	
	/**
	 * Deve essere chiamato DOPO {@link #setScale(float, float, float)}, 
	 * oppure devo tenere conto di scaleY in posY. E' come fare: 
	 * Node.getRelativeTransform().setPosition(posX, posY+scaleY, posZ), 
	 * ma in più vengono settati i parametri passati in MytNode
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public void setPosition(float posX, float posY, float posZ){
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		getRelativeTransform().setPosition(posX, posY+scaleY, posZ);
	}
	
	public void setPosition(SFVertex3f position){
		this.posX = position.getX();
		this.posY = position.getY();
		this.posZ = position.getZ();
		getRelativeTransform().setPosition(posX, posY+scaleY, posZ);
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public float getScaleZ() {
		return scaleZ;
	}
	
	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public float getPosZ() {
		return posZ;
	}

	public void getPosition(SFVertex3f position){
		position.getV()[0] = posX;
		position.getV()[1] = posY;
		position.getV()[2] = posZ;
	}
	
	/**
	 * Check if the node is covered by anotherNode along all the three axis using a cubic geometry.
	 * The test will be done if the node specify a model or he is created with {@link #Node(boolean)}
	 * @param anotherNode
	 * @return true if the node is covered by anotherNode, false otherwise
	 */
	public boolean coveredBy(MyNode anotherNode){
		if(anotherNode.getModel()!=null || anotherNode.isForceCoveredControl()){
			float[] position = new float[3];
			position[0]= posX;
			position[1]= posY+scaleY;
			position[2]= posZ;
			float[] scale = new float[3];
			scale[0]= scaleX;
			scale[1]= scaleY;
			scale[2]= scaleZ;
	
			float[] anotherPosition = new float[3];
			anotherPosition[0]= anotherNode.getPosX();
			anotherPosition[1]= anotherNode.getPosY()+anotherNode.getScaleY();
			anotherPosition[2]= anotherNode.getPosZ();
			float[] anotherScale = new float[3];
			anotherScale[0]= anotherNode.getScaleX();
			anotherScale[1]= anotherNode.getScaleY();
			anotherScale[2]= anotherNode.getScaleZ();
	
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
	public boolean coveredBySonNodes(MyNode anotherNode){
	    boolean covered = coveredBy(anotherNode);
	    if(covered) return true;
	    for(int i=0;i<anotherNode.getSonNodes().size();i++){
    		covered = coveredBySonNodes((MyNode)anotherNode.getSonNodes().get(i));
    		if(covered) return true;
        }
		return false;
	}
	
	public MyNode cloneSingleNodeWithoutSons() {
        SFTransform3f transform=new SFTransform3f();
        transform.set(getRelativeTransform());
        MyNode node = new MyNode(transform,getModel());
        node.setScale(scaleX, scaleY, scaleZ);
        node.setPosition(posX, posY, posZ);
		return node;
	}
}
