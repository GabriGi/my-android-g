package com.example.computergraphics.scenery;

import sfogl.integration.Model;

import com.example.computergraphics.MyNode;

/**See superclass {@link Scenery}) */
public class SceneryCubeAlone extends Scenery {
	
    /**
     * Uno scenario con un unico cubo al centro  //DEBUG
     */
	public SceneryCubeAlone(float avatarBody) {
		super(avatarBody);
	}
	
	@Override
	public MyNode getSceneryNode(Model wallModel, Model floorModel) {
		MyNode backgroundNode = new MyNode(wallModel);
		backgroundNode.setScale(0.75f,0.75f,0.75f);
		backgroundNode.setPosition(0,0,0);
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		backgroundNode.getSonNodes().add(new MyNode());
		return backgroundNode;
	}
}
