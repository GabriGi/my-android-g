package com.example.computergraphics.scenery;

import java.util.Random;

import com.example.computergraphics.MyNode;

import sfogl.integration.Model;
import shadow.math.SFMatrix3f;

/**See superclass {@link Scenery}) */
public class Scenery00 extends Scenery {
	
	static final private int NUMBER_OF_OSTACLE = 10;
    static final private float LUNGH_OBST = 0.25f;			//for each side (max = 1.374)
    protected float altezzObst;
	
    /**
     * Uno scenario a base quadrata con un pavimento, 4 mura (vedi la superclasse {@link Scenery}) 
     * e {@value #NUMBER_OF_OSTACLE} ostacoli a base quadrata in posizioni casuali.
     */
	public Scenery00(float avatarBody) {
		super(avatarBody);
    	altezzObst = avatarBody;
	}
	
	@Override
	public MyNode getSceneryNode(Model wallModel, Model floorModel) {
		MyNode backgroundNode = super.createBackgroundNode(wallModel, floorModel, LUNGH_MURO);
        
        MyNode innerObstaclesNode = createInnerObstacleNode(wallModel);
        innerObstaclesNode.setScale(1,1,1);
        innerObstaclesNode.setPosition(0,-1,0);
        backgroundNode.getSonNodes().add(innerObstaclesNode);
		
		return backgroundNode;
	}

	private MyNode createInnerObstacleNode(Model model1) {
		MyNode innerObstaclesNode = new MyNode();
		Random rand = new Random();
        for (int i = 0; i < NUMBER_OF_OSTACLE; i++) {
        	MyNode n = new MyNode(model1);
				float xp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
				float zp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
				n.setScale(LUNGH_OBST, altezzObst, LUNGH_OBST);
				n.setPosition(xp, 0, zp);
			innerObstaclesNode.getSonNodes().add(n);
		}
        return innerObstaclesNode;
	}
}
