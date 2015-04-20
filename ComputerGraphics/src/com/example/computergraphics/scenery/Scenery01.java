package com.example.computergraphics.scenery;

import com.example.computergraphics.MyNode;

import sfogl.integration.Model;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**See superclass {@link Scenery}) */
public class Scenery01 extends Scenery {

	private static final float ROOM_DIMENSION = 3.5f;
	private static final float ERR = 2*SPESS_MURO;	//This is to non overlap the inner walls
    private float altezzObst;
    
    /**
     * Uno scenario a base quadrata con un pavimento, 4 mura (vedi la superclasse {@link Scenery}) 
     * e un labirinto preconfigurato. Chiama {@link #getStartPosition()} per avere la posizione iniziale
     * e {@link #getFinalPosition()} per la posizione di arrivo
     */
	public Scenery01(float avatarBody) {
		super(avatarBody);
    	altezzObst = avatarBody;
	}
	
	@Override
	public float getRoomDimension() {
		return ROOM_DIMENSION;
	}
	
	@Override
	public SFVertex3f getStartPosition() {
		SFVertex3f v = super.getStartPosition();
		v.set3f(0, 0, -3);
		return v;
	}
	
	@Override
	public SFVertex3f getFinalPosition() {
		SFVertex3f v = super.getStartPosition();
		v.set3f(0, 0, 3);
		return v;
	}
	
	@Override
	public MyNode getSceneryNode(Model wallModel, Model floorModel) {
		enabledExternalWalls = startModel==null || finalModel==null;
		MyNode backgroundNode = super.createBackgroundNode(wallModel, floorModel, ROOM_DIMENSION);
        
        MyNode innerObstaclesNode = createInnerObstacleNode(wallModel);
        innerObstaclesNode.setScale(1,1,1);
        innerObstaclesNode.setPosition(0,-1,0);
        backgroundNode.getSonNodes().add(innerObstaclesNode);
		
		return backgroundNode;
	}

	private MyNode createInnerObstacleNode(Model model) {
		MyNode innerObstaclesNode = new MyNode();
		
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -2.5f, -0.5f, 0f, 1f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -2.5f, 2f, 0f, 0.5f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -1.5f, -2f, 0f, 0.5f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -1.5f, 0f, 0f, 0.5f-ERR, altezzObst));
		
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -0.5f, -3f, 0f, 0.5f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -0.5f, 2.5f, 0f, 1f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 0.5f, -3f, 0f, 0.5f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 0.5f, 3f, 0f, 0.5f-ERR, altezzObst));
		
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 1.5f, -1f, 0f, 0.5f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 1.5f, 1f, 0f, 0.5f-ERR, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 2.5f, 2.5f, 0f, 1f-ERR, altezzObst));
		
		

		innerObstaclesNode.getSonNodes().add(createWallNode(model, -2.5f, -2.5f, 1f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 1.5f, -2.5f, 1f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -1f, -1.5f, 0.5f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 2f, -1.5f, 1.5f, 0f, altezzObst));
		
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 0.5f, -0.5f, 2f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -2f, 0.5f, 0.5f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 0.5f, 0.5f, 1f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 3f, 0.5f, 0.5f, 0f, altezzObst));
		
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -0.5f, 1.5f, 2f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, -2f, 2.5f, 0.5f, 0f, altezzObst));
		innerObstaclesNode.getSonNodes().add(createWallNode(model, 1f, 2.5f, 0.5f, 0f, altezzObst));
		
		if(startModel!=null && finalModel!=null){
			innerObstaclesNode.getSonNodes().add(createWallNode(model, -3.5f, 0f, 0f, 3.5f-ERR, altezzObst));
			innerObstaclesNode.getSonNodes().add(createWallNode(model, 3.5f, 0f, 0f, 3.5f-ERR, altezzObst));
			
			innerObstaclesNode.getSonNodes().add(createWallNode(model, -2f, -3.5f, 1.5f, 0f, altezzObst));
			innerObstaclesNode.getSonNodes().add(createWallNode(startModel, 0, -3.5f, 0.5f-ERR, 0f, altezzObst));
			innerObstaclesNode.getSonNodes().add(createWallNode(model, 2f, -3.5f, 1.5f, 0f, altezzObst));
			innerObstaclesNode.getSonNodes().add(createWallNode(model, -2f, 3.5f, 1.5f, 0f, altezzObst));
			innerObstaclesNode.getSonNodes().add(createWallNode(finalModel, 0, 3.5f, 0.5f-ERR, 0f, altezzObst));
			innerObstaclesNode.getSonNodes().add(createWallNode(model, 2f, 3.5f, 1.5f, 0f, altezzObst));
		}
		
        return innerObstaclesNode;
	}
}
