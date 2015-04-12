package com.example.computergraphics.scenery;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Uno scenario a base quadrata con un pavimento e 4 mura.
 */
public class Scenery {

    static final protected float LUNGH_MURO = 3.00f;			//for each side
    static final protected float SPESS_MURO = 0.05f;			//for each side (is good also with 0f)
    protected float altezzMuro;
	
    public Scenery(float avatarBody) {
		altezzMuro = avatarBody*2.5f;
	}
    
    public float getRoomDimension() {
		return LUNGH_MURO;
	}
	
	public SFVertex3f getStartPosition() {
		return new SFVertex3f(0,0,0);
	}
	
	public SFVertex3f getFinalPosition() {
		return new SFVertex3f(0,0,0);
	}
	
	public Node getSceneryNode(Model model) {
		Node sceneryNode = createBackgroundNode(model, LUNGH_MURO);
		return sceneryNode;
	}
    
	protected Node createBackgroundNode(Model model, float sceneryDimension) {
		Node backgroundNode = new Node();
        
		Node floorNode=new Node(model);
        floorNode.getRelativeTransform().setPosition(0.0f, -SPESS_MURO, 0.0f);
        floorNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(sceneryDimension+SPESS_MURO,SPESS_MURO,sceneryDimension+SPESS_MURO));
        backgroundNode.getSonNodes().add(floorNode);

        backgroundNode.getSonNodes().add(createWallNode(model, sceneryDimension, 0, 0, sceneryDimension, altezzMuro));
        backgroundNode.getSonNodes().add(createWallNode(model, 0, sceneryDimension, sceneryDimension, 0, altezzMuro));
        backgroundNode.getSonNodes().add(createWallNode(model, -sceneryDimension, 0, 0, sceneryDimension, altezzMuro));
        backgroundNode.getSonNodes().add(createWallNode(model, 0, -sceneryDimension, sceneryDimension, 0, altezzMuro));
		
		backgroundNode.getRelativeTransform().setPosition(0,0,0);
		backgroundNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(1,1,1));
        
        return backgroundNode;
	}
	
	protected Node createWallNode(Model model, float x, float z, float sx, float sz, float h){
		Node wall1Node=new Node(model);
        wall1Node.getRelativeTransform().setPosition(x, h, z);
        wall1Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(sx+SPESS_MURO, h, sz+SPESS_MURO));
        return wall1Node;
	}
}
