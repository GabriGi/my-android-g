package com.example.computergraphics.scenery;

import com.example.computergraphics.MyNode;

import sfogl.integration.Model;
import shadow.math.SFMatrix3f;

/**See superclass {@link Scenery}) */
public class SceneryCubeAlone extends Scenery {
	
    /**
     * Uno scenario con un unico cubo al centro  //DEBUG
     */
	public SceneryCubeAlone(float avatarBody) {
		super(avatarBody);
	}
	
	@Override
	public MyNode getSceneryNode(Model model) {
		MyNode backgroundNode = new MyNode(model);
		backgroundNode.getRelativeTransform().setPosition(0,0.75f,0);
		backgroundNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.75f,0.75f,0.75f));
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		return backgroundNode;
	}
}
