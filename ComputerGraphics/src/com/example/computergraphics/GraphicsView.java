package com.example.computergraphics;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.SFCamera;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.computergraphics.controls.ActionSet;
import com.example.computergraphics.controls.BasicController;
import com.example.computergraphics.controls.IController;
import com.example.computergraphics.controls.ProxyController;
import com.example.computergraphics.scenery.Scenery;
import com.example.computergraphics.scenery.Scenery00;
import com.example.computergraphics.scenery.Scenery01;

/**
 * Created by Alessandro on 13/03/15.
 * Modified by Gabriele since 01/04/15
 */
public class GraphicsView extends GLSurfaceView{

    static final private float AVAT_BODY = 0.25f;			//Avatar's body height and width for each side
    
	private boolean enableTouching = false;
	
    private Context context;
    private ActionSet actionSet;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleDetector;
    private ProxyController controller;
    
    private float widthRatio;
    private float heightRatio;
    
    private ArrayList<Scenery> sceneryList;
	private int sceneryNumber = 0;
	private boolean changingScenary = true;
    
    public GraphicsView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        this.context=context;
        //super.setEGLConfigChooser(8,8,8,8,16,0);
        
        controller = new ProxyController(new BasicController(BasicController.ABSOLUTE_MODE));
        gestureDetector = new GestureDetector(context, controller);
        gestureDetector.setOnDoubleTapListener(controller);
        gestureDetector.setIsLongpressEnabled(false);		//Solo se sto usando il BasicController!!!
        scaleDetector = new ScaleGestureDetector(context, controller);
        
        sceneryList = new ArrayList<Scenery>();
    	sceneryList.add(new Scenery00(AVAT_BODY));
    	sceneryList.add(new Scenery01(AVAT_BODY));
    	sceneryNumber = 1;
    	
        setRenderer(new GraphicsRenderer());
    }
    
    public boolean isEnableTouching() {
		return enableTouching;
	}
    
    public int getNumberOfScenery() {
    	return sceneryList.size();
    }
    
    public void setController(IController controller, boolean isLongpressEnabled) {
        actionSet.stopMoving();
		this.controller.setController(controller);
        gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
        this.controller.setViewSize(this.getWidth(), this.getHeight());
        this.controller.setActionsSet(actionSet);
	}
    
    public boolean setSceneryNumber(int sceneryNumberSelected) {
    	if(sceneryNumberSelected<sceneryList.size()){
    		if(actionSet!=null) actionSet.stopMoving();
			this.sceneryNumber = sceneryNumberSelected;
			Log.d("task", "Change scenary");
			enableTouching = false;
			onPause();
			changingScenary = true;
			onResume();
	    	return true;
    	}else{
    		//Out of bounds exception
    		return false;
    	}
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(enableTouching){
	    	super.onTouchEvent(event);
	    	scaleDetector.onTouchEvent(event);
	    	if(!scaleDetector.isInProgress()){
	    		gestureDetector.onTouchEvent(event);
	    		if((event.getAction() == MotionEvent.ACTION_UP) && controller.isScrolling()){
	    			controller.stopScrolling();
	    		}
	    	}
	    	if(actionSet.getFlingEvent()!=ActionSet.FLING_EVENT_NULL) invalidate();
    	}
    	return true;
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	widthRatio = w/(float)Math.max(w, h);
    	heightRatio = h/(float)Math.max(w, h);
    	controller.setViewSize(w,h);
    }
    
    @Override
    public void computeScroll() {
    	super.computeScroll();
    	if(actionSet!= null){
	    	if(actionSet.getFlingEvent()==ActionSet.FLING_EVENT_CAMERA){
	    		actionSet.flingCamera();
	    		invalidate();
	    	}
    	}
    }
    
    public class GraphicsRenderer implements Renderer{
    	
        
    	private float scale = ActionSet.SCALE_DEF;
    	private float rotX = ActionSet.ROT_X_DEF;	//It represent the floor inclination.
    	private float rotY = 0.0f;					//The user will change it at runtime
    	//private float rotZ = 0.0f;				//This must be 0.0f.
        
    	private Node node;
        private SFCamera cam;
        private SFCamera startCam;
    	private ArrayList<Model> models;
        
        private ShadingProgram program;
        
        private SFCamera setupCam(){
        	float distAvatarCam = 3*AVAT_BODY;	// = AVATAR_BODY = 0.25f
        	SFVertex3f focus = new SFVertex3f(0, 0, (-1f-AVAT_BODY-distAvatarCam));
			SFVertex3f dir = new SFVertex3f(0, 0, 1);
			SFVertex3f left = new SFVertex3f(1, 0, 0);
			SFVertex3f up = new SFVertex3f(0, 1, 0);
			float leftL = widthRatio/scale;
			float upL = heightRatio/scale;
			float distance = 10f;
			
			SFCamera cam = new SFCamera(focus, dir, left, up, leftL, upL, distance);
			cam.setDelta(0.01f);
			cam.setPerspective(true);
			return cam;
        }

		private Node createAvatarNode(Model model) {
			Node avatarNode = new Node();
            
            Node bodyNode =new Node(model);
            bodyNode.getRelativeTransform().setPosition(0.0f, AVAT_BODY, 0.0f);
            bodyNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(AVAT_BODY,AVAT_BODY,AVAT_BODY));
            avatarNode.getSonNodes().add(bodyNode);

            Node neckNode = new Node(model);
            neckNode.getRelativeTransform().setPosition(0.0f, 0.55f, 0.0f);
            neckNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.15f,0.05f,0.15f));
            avatarNode.getSonNodes().add(neckNode);
            
            Node headNode = new Node(model);
            headNode.getRelativeTransform().setPosition(0.0f, 0.8f, 0.0f);
            headNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.2f,0.2f,0.2f));
            avatarNode.getSonNodes().add(headNode);

            avatarNode.getRelativeTransform().setPosition(0,0,0);
            avatarNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(1,1,1));
            
			return avatarNode;
		}

		private void setupNodeStructure(ArrayList<Model> models) {
			
			node.getRelativeTransform().setPosition(0, 0, 0);
			 
			Node avatarNode, backgroundNode=null;
            
            if (sceneryNumber!=0) {
            	backgroundNode = sceneryList.get(sceneryNumber).getSceneryNode(models.get(0));
			}
            
            avatarNode = createAvatarNode(models.get(1));
            avatarNode.getRelativeTransform().setPosition(sceneryList.get(sceneryNumber).getStartPosition());
            node.getSonNodes().add(avatarNode);
            
            if(sceneryNumber!=0){
            	if(avatarNode.getSonNodes().get(0).coveredBySonNodes(backgroundNode)){ 
            		Log.d("ERRORE", "La posizione di partenza dell'Avatar è dentro un ostacolo!");
				}
            }else{
	            do{ 
	            	backgroundNode = sceneryList.get(0).getSceneryNode(models.get(0));
				}while(avatarNode.getSonNodes().get(0).coveredBySonNodes(backgroundNode));
            }
			node.getSonNodes().add(backgroundNode);
		}

		private ArrayList<Model> createModels() {
        	ArrayList<Model> models = new ArrayList<Model>();
			//Step 1 : load Shading effects
            ShadersKeeper.loadPipelineShaders(context);
            program= ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);

            //Step 2 : load Textures
            int textureModel=SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB,
                    GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
            BitmapTexture texture = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.paddedroomtexture01), textureModel);
            texture.init();
            
            //Step 3 : create a Material (materials combine shaders+textures+shading parameters)
            Material material=new Material(program);
            material.getTextures().add(texture);

            //Step 4: load a Geometry
            ArrayObject[] objects = ObjLoader.arrayObjectFromFile(context, "Cube.obj");

            Mesh mesh=new Mesh(objects[0]);
            mesh.init();

            //Step 5: create a Model combining material+geometry
            Model model1=new Model();
            model1.setRootGeometry(mesh);
            model1.setMaterialComponent(material);
            models.add(model1);
            
            //Step 2-5bis: do the same for the background
            BitmapTexture texture2 = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.bluepaddedroomtexture01), textureModel);
            texture2.init();
            
            Material material2=new Material(program);
            material2.getTextures().add(texture2);

            Mesh mesh2=new Mesh(objects[0]);
            mesh2.init();

            Model model2=new Model();
            model2.setRootGeometry(mesh2);
            model2.setMaterialComponent(material2);
            models.add(model2);
            
			return models;
		}

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	        models = createModels();
        	if(changingScenary){
        		if(node==null){	//It is the first lunch, so I have to create anything.
		            //Step 6: create a Node, that is a reference system where you can place your Models
		            node = new Node();
		            
		            setupNodeStructure(models);
		            
		        	startCam = setupCam();
		        	cam = setupCam();
		            actionSet = new ActionSet(context, node, cam);
		            controller.setActionsSet(actionSet);
        		}else{
        			actionSet.stopMovingAndJumping();
        			node.removeAllSonNodes();
        			setupNodeStructure(models);
        		}
    			actionSet.setRoomDimension(sceneryList.get(sceneryNumber).getRoomDimension());
    			actionSet.restoreDefaultValues();
        		changingScenary = false;
        	}
            enableTouching = true;
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

            SFOGLSystemState.cleanupColorAndDepth(1, 1, 0, 1);
            
            //setup the View Projection
        	
			scale = actionSet.getScale();
			rotX = actionSet.getRotX();
			rotY = actionSet.getRotY();
			
			float upL =heightRatio/scale;
			SFVertex3f focus = SFMatrix3f.getRotationX(-rotX).Mult(startCam.getF());
          	focus.add3f(new SFVertex3f(0, 3*AVAT_BODY, 0));
          	if(focus.getY()<upL+2*AVAT_BODY) focus.setY(upL+2*AVAT_BODY);	//Per non tagliare gli ostacoli
			//SFVertex3f dir = SFMatrix3f.getRotationX(rotX).Mult(startCam.getDir());
          	
			cam.set(SFMatrix3f.getRotationY(rotY).Mult(focus), 
					SFMatrix3f.getRotationY(rotY).Mult(startCam.getDir()), 	//dir), 
					SFMatrix3f.getRotationY(rotY).Mult(startCam.getLeft()), 
					startCam.getUp(), 
					widthRatio/scale, 
					upL, 
					startCam.getDistance());
			//cam.setDir(SFMatrix3f.getRotationX(-rotX).Mult(cam.getDir()));
			
			//TODO Scaling e rotY si possono migliorare.
            
        	cam.update();
            program.setupProjection(cam.extractTransform());
            
            if(cam.getDir().getZ()>Math.abs(cam.getDir().getX())){				//Se punto verso l'alto..
            	node.getSonNodes().get(1).getSonNodes().get(1).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(2).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(3).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(4).setEnabled(false);	//..rimuovo il muro in basso
            } else if(cam.getDir().getZ()<-Math.abs(cam.getDir().getX())){		//Se punto verso il basso..
            	node.getSonNodes().get(1).getSonNodes().get(1).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(2).setEnabled(false);	//..rimuovo il muro in alto
            	node.getSonNodes().get(1).getSonNodes().get(3).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(4).setEnabled(true);
            } else if(cam.getDir().getX()>Math.abs(cam.getDir().getZ())){		//Se punto verso destra..
            	node.getSonNodes().get(1).getSonNodes().get(1).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(2).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(3).setEnabled(false);	//..rimuovo il muro a sinistra
            	node.getSonNodes().get(1).getSonNodes().get(4).setEnabled(true);
            } else if(cam.getDir().getX()<-Math.abs(cam.getDir().getZ())){		//Se punto verso sinistra..
            	node.getSonNodes().get(1).getSonNodes().get(1).setEnabled(false);	//..rimuovo il muro a destra
            	node.getSonNodes().get(1).getSonNodes().get(2).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(3).setEnabled(true);
            	node.getSonNodes().get(1).getSonNodes().get(4).setEnabled(true);
            }
        	
            node.updateTree(new SFTransform3f());

            //Draw the node
            node.draw();
            
            //int[] viewport=new int[4];
            //GLES20.glGetIntegerv(GLES20.GL_VIEWPORT,viewport,0);
            //Log.e("Graphics View Size", Arrays.toString(viewport));
        }
    }
}
