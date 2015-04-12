package com.example.computergraphics.scenery;

import java.util.Random;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Uno scenario a base quadrata con un pavimento, 4 mura (vedi la superclasse {@link Scenery}) 
 * e {@value #NUMBER_OF_OSTACLE} ostacoli a base quadrata in posizioni casuali.
 */
public class Scenery00 extends Scenery {
	
	static final private int NUMBER_OF_OSTACLE = 10;
    static final private float LUNGH_OBST = 0.25f;			//for each side (max = 1.374)
    protected float altezzObst;
	
	public Scenery00(float avatarBody) {
		super(avatarBody);
    	altezzObst = avatarBody;
	}
	
	@Override
	public SFVertex3f getStartPosition() {
		SFVertex3f v = super.getStartPosition();
		v.set3f(0, 0, 0);
		return v;
	}
	
	@Override
	public Node getSceneryNode(Model model) {
		Node backgroundNode = super.getSceneryNode(model);
        
        Node innerObstaclesNode = createInnerObstacleNode(model);
        innerObstaclesNode.getRelativeTransform().setPosition(0,0,0);
        innerObstaclesNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(1,1,1));
        backgroundNode.getSonNodes().add(innerObstaclesNode);
		
		return backgroundNode;
	}

	private Node createInnerObstacleNode(Model model1) {
		Node innerObstaclesNode = new Node();
		Random rand = new Random();
        for (int i = 0; i < NUMBER_OF_OSTACLE; i++) {
        	Node n = new Node(model1);
				float xp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
				float zp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
				n.getRelativeTransform().setPosition(xp, altezzObst, zp);
				n.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_OBST,altezzObst,LUNGH_OBST));
			innerObstaclesNode.getSonNodes().add(n);
		}
        return innerObstaclesNode;
	}
}
