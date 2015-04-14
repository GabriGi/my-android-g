package com.example.computergraphics.scenery;

import sfogl.integration.Model;
import sfogl.integration.Node;
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
	public Node getSceneryNode(Model model) {
		Node backgroundNode = new Node(model);
		backgroundNode.getRelativeTransform().setPosition(0,0.75f,0);
		backgroundNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.75f,0.75f,0.75f));
		backgroundNode.getSonNodes().add(new Node());
		backgroundNode.getSonNodes().add(new Node());
		backgroundNode.getSonNodes().add(new Node());
		backgroundNode.getSonNodes().add(new Node());
		backgroundNode.getSonNodes().add(new Node());
		return backgroundNode;
	}
}
