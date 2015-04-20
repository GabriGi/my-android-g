package com.example.computergraphics.scenery;

import sfogl.integration.Model;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

import com.example.computergraphics.MyNode;

/**
 * Uno scenario a base quadrata con un pavimento e 4 mura.
 */
public class Scenery {

    static final protected float LUNGH_MURO = 3.00f;			//for each side
    static final protected float SPESS_MURO = 0.05f;			//for each side (is good also with 0f)
    protected float altezzMuro;
    protected Model startModel;
    protected Model finalModel;
    protected boolean enabledExternalWalls = true;
	
    public Scenery(float avatarBody) {
		altezzMuro = avatarBody*2.5f;
	}
    public boolean isEnabledExternalWalls() {
		return enabledExternalWalls;
    }
    
    public float getRoomDimension() {
		return LUNGH_MURO;
	}
	
	public SFVertex3f getStartPosition() {
		return new SFVertex3f(0,0,0);
	}
	
	public SFVertex3f getFinalPosition() {
		return new SFVertex3f(0,LUNGH_MURO,0);
	}
	
	/**
	 * Call this BEFORE calling {@link #getSceneryNode(Model)} if you want to visualize the start position
	 * @param startModel the model used to visualize the start position.
	 */
	public void setStartModel(Model startModel) {
		this.startModel = startModel;
	}

	/**
	 * Call this BEFORE calling {@link #getSceneryNode(Model)} if you want to visualize the final position
	 * @param finalModel the model used to visualize the final position.
	 */
	public void setFinishModel(Model finalModel) {
		this.finalModel = finalModel;
	}
	/**
	 * Create the scenery. If you are interested to visualize start or final postion, 
	 * call {@link #setStartModel(Model)} or {@link #setFinishModel(Model) BEFORE calling this method.
	 * @param model the model used to visualize the scenery.
	 */
	public MyNode getSceneryNode(Model wallModel, Model floorModel) {
		MyNode sceneryNode = createBackgroundNode(wallModel, floorModel, LUNGH_MURO);
		return sceneryNode;
	}
    
	protected MyNode createBackgroundNode(Model wallModel, Model floorModel, float sceneryDimension) {
		MyNode backgroundNode = new MyNode();
        
		MyNode floorNode=new MyNode(floorModel);
        floorNode.setScale(sceneryDimension+SPESS_MURO, SPESS_MURO, sceneryDimension+SPESS_MURO);
        floorNode.setPosition(0.0f, -SPESS_MURO*2, 0.0f);
        backgroundNode.getSonNodes().add(floorNode);
        
        backgroundNode.getSonNodes().add(createWallNode(wallModel, sceneryDimension, 0, 0, sceneryDimension, altezzMuro));
        backgroundNode.getSonNodes().add(createWallNode(wallModel, 0, sceneryDimension, sceneryDimension, 0, altezzMuro));
        backgroundNode.getSonNodes().add(createWallNode(wallModel, -sceneryDimension, 0, 0, sceneryDimension, altezzMuro));
        backgroundNode.getSonNodes().add(createWallNode(wallModel, 0, -sceneryDimension, sceneryDimension, 0, altezzMuro));
        if(!enabledExternalWalls){
        	for (int i = 1; i < 5; i++) {
        		backgroundNode.getSonNodes().get(i).setEnabled(false);
			}
        }
		backgroundNode.setScale(1,1,1);
		backgroundNode.setPosition(0,-1,0);
        
        return backgroundNode;
	}
	
	protected MyNode createWallNode(Model model, float x, float z, float sx, float sz, float h){

//        Mesh mesh=model.getGeometry();
//        Material material=model.getMaterial();
//        
//        ArrayObject array=mesh.getArrayObject();
//        
//        Model modelClone=new Model();
//        modelClone.setMaterialComponent(material);
//        Mesh meshClone=new Mesh(array.cloneScaled(sx+SPESS_MURO, h, sz+SPESS_MURO));
//        modelClone.setRootGeometry(meshClone);
		
		MyNode wall1Node=new MyNode(model);//Clone);
//        wall1Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(1,1,1));
        wall1Node.setScale(sx+SPESS_MURO, h, sz+SPESS_MURO);
        wall1Node.setPosition(x, 0, z);
        return wall1Node;
	}
}
