package com.example.computergraphics;

import java.util.Random;

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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.computergraphics.controls.ActionSet;
import com.example.computergraphics.controls.BasicController;
import com.example.computergraphics.controls.IController;
import com.example.computergraphics.controls.ProxyController;

/**
 * Created by Alessandro on 13/03/15.
 * Modified by Gabriele since 01/04/15
 */
public class GraphicsView extends GLSurfaceView{

    private Context context;
    private ActionSet actionSet;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleDetector;
    private ProxyController controller;
    
    private float widthRatio;
    private float heightRatio;
	
    
    public GraphicsView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        this.context=context;
        //super.setEGLConfigChooser(8,8,8,8,16,0);
        setRenderer(new GraphicsRenderer());
        
        controller = new ProxyController(new BasicController(BasicController.ABSOLUTE_MODE));
        gestureDetector = new GestureDetector(context, controller);
        gestureDetector.setOnDoubleTapListener(controller);
        gestureDetector.setIsLongpressEnabled(false);		//Solo se sto usando il BasicController!!!
        scaleDetector = new ScaleGestureDetector(context, controller);
    }
    
    public void setController(IController controller, boolean isLongpressEnabled) {
		this.controller.setController(controller);
        gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
        this.controller.setViewSize(this.getWidth(), this.getHeight());
        this.controller.setActionsSet(actionSet);
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	scaleDetector.onTouchEvent(event);
    	if(!scaleDetector.isInProgress()){
    		gestureDetector.onTouchEvent(event);
    		if((event.getAction() == MotionEvent.ACTION_UP) && controller.isScrolling()){
    			controller.stopScrolling();
    		}
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
    
//    @Override
//    public void computeScroll() {
//    	super.computeScroll();
//    	if(actionSet!= null){
//	    	if(actionSet.flingCircle()){
//	    		//ViewCompat.postInvalidateOnAnimation(this);
//	    		invalidate();	//funziona comunque..
//	    	}
//    	}
//    }
    
    public class GraphicsRenderer implements Renderer{
    	
        static final private float AVAT_SCALE = 1;				//Must be 1
        static final private float AVAT_BODY = 0.25f;			//Avatar's body height and width for each side
        
        static final private float LUNGH_MURO = 3.00f;			//for each side
        static final private float SPESS_MURO = 0.05f;			//for each side (is good also with 0f)
        static final private float ALTEZ_MURO = AVAT_BODY*2.5f;	//for each side 
        
        static final private float ALTEZ_OBST = AVAT_BODY;		//for each side
        static final private float LUNGH_OBST = 0.25f;			//for each side (max = 1.374)
    	static final private int NUMBER_OF_OSTACLE = 10;
    	
    	private float scale = ActionSet.SCALE_DEF;
    	private float rotX = ActionSet.ROT_X_DEF;	//It represent the floor inclination.
    	private float rotY = 0.0f;					//The user will change it at runtime
    	//private float rotZ = 0.0f;				//This must be 0.0f.
        
    	private Node node;
        private SFCamera cam;
        private SFCamera startCam;
        
        private ShadingProgram program;
        
        private SFCamera setupCam(){
        	float distAvatarCam = 3*ALTEZ_OBST;	// = AVATAR_BODY = 0.25f
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
            
			return avatarNode;
		}
		
		private Node createBackgroundNode(Model model) {
			Node backgroundNode = new Node();
            
			Node floorNode=new Node(model);
	        floorNode.getRelativeTransform().setPosition(0.0f, -SPESS_MURO, 0.0f);
	        floorNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,SPESS_MURO,LUNGH_MURO));
	        backgroundNode.getSonNodes().add(floorNode);
	        
	        Node wall1Node=new Node(model);
	        wall1Node.getRelativeTransform().setPosition(LUNGH_MURO-SPESS_MURO, ALTEZ_MURO, 0.0f);
	        wall1Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(SPESS_MURO,ALTEZ_MURO,LUNGH_MURO));
	        backgroundNode.getSonNodes().add(wall1Node);
	        
	        Node wall2Node=new Node(model);
	        wall2Node.getRelativeTransform().setPosition(0.0f, ALTEZ_MURO, LUNGH_MURO-SPESS_MURO);
	        wall2Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,ALTEZ_MURO,SPESS_MURO));
	        backgroundNode.getSonNodes().add(wall2Node);
	        
	        Node wall3Node=new Node(model);
	        wall3Node.getRelativeTransform().setPosition(-LUNGH_MURO+SPESS_MURO, ALTEZ_MURO, 0.0f);
	        wall3Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(SPESS_MURO,ALTEZ_MURO,LUNGH_MURO));
	        backgroundNode.getSonNodes().add(wall3Node);
	        
	        Node wall4Node=new Node(model);
	        wall4Node.getRelativeTransform().setPosition(0.0f, ALTEZ_MURO, -LUNGH_MURO+SPESS_MURO);
	        wall4Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,ALTEZ_MURO,SPESS_MURO));
	        backgroundNode.getSonNodes().add(wall4Node);
	        
	        return backgroundNode;
		}

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

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

            //Step 6: create a Node, that is a reference system where you can place your Model
            node = new Node();
            node.getRelativeTransform().setPosition(0, 0, 0);
            
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
            
            /* *************************************************************** */

            Node avatarNode = createAvatarNode(model2);
            avatarNode.getRelativeTransform().setPosition(0, 0, 0);
            avatarNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(AVAT_SCALE,AVAT_SCALE,AVAT_SCALE));
            node.getSonNodes().add(avatarNode);
            
            /* *************************************************************** */
            Node backgroundNode = createBackgroundNode(model1);
            backgroundNode.getRelativeTransform().setPosition(0, 0, 0);
            backgroundNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(AVAT_SCALE,AVAT_SCALE,AVAT_SCALE));
            node.getSonNodes().add(backgroundNode);
            
            Node innerObstaclesNode = new Node();
            innerObstaclesNode.getRelativeTransform().setPosition(0, 0, 0);
            innerObstaclesNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(1, 1, 1));
            backgroundNode.getSonNodes().add(innerObstaclesNode);
            
            Random rand = new Random();
            for (int i = 0; i < NUMBER_OF_OSTACLE; i++) {
            	Node n;
				while(true){
					n = new Node(model1);
					float xp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
					float zp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
					n.getRelativeTransform().setPosition(xp, ALTEZ_OBST, zp);
					n.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_OBST,ALTEZ_OBST,LUNGH_OBST));
					if(!avatarNode.getSonNodes().get(0).coveredBy(n)) break;
				}
				innerObstaclesNode.getSonNodes().add(n);
			}
        	startCam = setupCam();
        	cam = setupCam();
            actionSet = new ActionSet(context, node, cam);
            controller.setActionsSet(actionSet);
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
          	focus.add3f(new SFVertex3f(0, 3*ALTEZ_OBST, 0));
          	if(focus.getY()<upL+2*ALTEZ_OBST) focus.setY(upL+2*ALTEZ_OBST);	//Per non tagliare gli ostacoli
//			SFVertex3f dir = SFMatrix3f.getRotationX(rotX).Mult(startCam.getDir());
          	
			cam.set(SFMatrix3f.getRotationY(rotY).Mult(focus), 
					SFMatrix3f.getRotationY(rotY).Mult(startCam.getDir()), 	//dir), 
					SFMatrix3f.getRotationY(rotY).Mult(startCam.getLeft()), 
					startCam.getUp(), 
					widthRatio/scale, 
					upL, 
					startCam.getDistance());
//			cam.setDir(SFMatrix3f.getRotationX(-rotX).Mult(cam.getDir()));
			
//			Log.d("task", "rotX "+rotX+"; scale "+scale);
            
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
