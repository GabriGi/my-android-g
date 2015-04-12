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
    
    public static float getSceneryDimension() {
		return LUNGH_MURO;
	}
	
	public SFVertex3f getStartPosition() {
		return new SFVertex3f(0,0,0);
	}
	
	public Node getSceneryNode(Model model) {
		Node sceneryNode = createBackgroundNode(model);
		return sceneryNode;
	}
    
	private Node createBackgroundNode(Model model) {
		Node backgroundNode = new Node();
        
		Node floorNode=new Node(model);
        floorNode.getRelativeTransform().setPosition(0.0f, -SPESS_MURO, 0.0f);
        floorNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,SPESS_MURO,LUNGH_MURO));
        backgroundNode.getSonNodes().add(floorNode);
        
        Node wall1Node=new Node(model);
        wall1Node.getRelativeTransform().setPosition(LUNGH_MURO-SPESS_MURO, altezzMuro, 0.0f);
        wall1Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(SPESS_MURO,altezzMuro,LUNGH_MURO));
        backgroundNode.getSonNodes().add(wall1Node);
        
        Node wall2Node=new Node(model);
        wall2Node.getRelativeTransform().setPosition(0.0f, altezzMuro, LUNGH_MURO-SPESS_MURO);
        wall2Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,altezzMuro,SPESS_MURO));
        backgroundNode.getSonNodes().add(wall2Node);
        
        Node wall3Node=new Node(model);
        wall3Node.getRelativeTransform().setPosition(-LUNGH_MURO+SPESS_MURO, altezzMuro, 0.0f);
        wall3Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(SPESS_MURO,altezzMuro,LUNGH_MURO));
        backgroundNode.getSonNodes().add(wall3Node);
        
        Node wall4Node=new Node(model);
        wall4Node.getRelativeTransform().setPosition(0.0f, altezzMuro, -LUNGH_MURO+SPESS_MURO);
        wall4Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,altezzMuro,SPESS_MURO));
        backgroundNode.getSonNodes().add(wall4Node);

		backgroundNode.getRelativeTransform().setPosition(0,0,0);
		backgroundNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(1,1,1));
        
        return backgroundNode;
	}
}
