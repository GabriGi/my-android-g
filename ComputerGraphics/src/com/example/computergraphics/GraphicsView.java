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
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Alessandro on 13/03/15.
 * Modified by Gabriele since 01/04/15
 */
public class GraphicsView extends GLSurfaceView{

    private Context context;

    private float t=0;
    private float add = 0.01f;
    
    public GraphicsView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        this.context=context;
        //super.setEGLConfigChooser(8,8,8,8,16,0);
        setRenderer(new GraphicsRenderer());
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	// TODO Auto-generated method stub
    	//TODO lavorare su add, t ecc..
    	return true;
    }

    public class GraphicsRenderer implements Renderer{

    	private Node node;
    	private Node avatarNode;
    	private Node backgroundNode;
    	private Node obstaclesNode;
    	
    	static final private int MAX_NUMBER_OF_OSTACLE = 10;
        static final private float LUNGH_MURO = 3.00f;	//for each side
        static final private float SPESS_MURO = 0.05f;	//for each side
        static final private float ALTEZ_MURO = 0.25f;	//for each side
        static final private float LUNGH_OBST = 0.25f;	//for each side


        private ShadingProgram program;

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
            node=new Node();
            //node.getRelativeTransform().setPosition(0, -0.5f, 0);
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
            
            avatarNode=new Node();
            avatarNode.getRelativeTransform().setPosition(0, 0, 0);
            node.getSonNodes().add(avatarNode);
            
            Node bodyNode=new Node();
            bodyNode.setModel(model2);
            bodyNode.getRelativeTransform().setPosition(0.0f, 0.25f, 0.0f);
            bodyNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.25f,0.25f,0.25f));
            avatarNode.getSonNodes().add(bodyNode);

            Node neckNode=new Node();
            neckNode.setModel(model2);
            neckNode.getRelativeTransform().setPosition(0.0f, 0.55f, 0.0f);
            neckNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.15f,0.05f,0.15f));
            avatarNode.getSonNodes().add(neckNode);
            
            Node headNode=new Node();
            headNode.setModel(model2);
            headNode.getRelativeTransform().setPosition(0.0f, 0.8f, 0.0f);
            headNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.2f,0.2f,0.2f));
            avatarNode.getSonNodes().add(headNode);

            /* *************************************************************** */
            
            backgroundNode=new Node();
            backgroundNode.getRelativeTransform().setPosition(0, 0, 0);
            node.getSonNodes().add(backgroundNode);
            
            Node floorNode=new Node();
            floorNode.setModel(model1);
            floorNode.getRelativeTransform().setPosition(0.0f, -SPESS_MURO, 0.0f);
            floorNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,SPESS_MURO,LUNGH_MURO));
            backgroundNode.getSonNodes().add(floorNode);
            
            Node wall1Node=new Node();
            wall1Node.setModel(model1);
            wall1Node.getRelativeTransform().setPosition(LUNGH_MURO-SPESS_MURO, ALTEZ_MURO, 0.0f);
            wall1Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(SPESS_MURO,ALTEZ_MURO,LUNGH_MURO));
            backgroundNode.getSonNodes().add(wall1Node);
            
            Node wall2Node=new Node();
            wall2Node.setModel(model1);
            wall2Node.getRelativeTransform().setPosition(0.0f, ALTEZ_MURO, LUNGH_MURO-SPESS_MURO);
            wall2Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,ALTEZ_MURO,SPESS_MURO));
            backgroundNode.getSonNodes().add(wall2Node);
            
            Node wall3Node=new Node();
            wall3Node.setModel(model1);
            wall3Node.getRelativeTransform().setPosition(-LUNGH_MURO+SPESS_MURO, ALTEZ_MURO, 0.0f);
            wall3Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(SPESS_MURO,ALTEZ_MURO,LUNGH_MURO));
            backgroundNode.getSonNodes().add(wall3Node);
            
            Node wall4Node=new Node();
            wall4Node.setModel(model1);
            wall4Node.getRelativeTransform().setPosition(0.0f, ALTEZ_MURO, -LUNGH_MURO+SPESS_MURO);
            wall4Node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_MURO,ALTEZ_MURO,SPESS_MURO));
            backgroundNode.getSonNodes().add(wall4Node);

            /* *************************************************************** */
            
            obstaclesNode=new Node();
            obstaclesNode.getRelativeTransform().setPosition(0, 0, 0);
            node.getSonNodes().add(obstaclesNode);
            
            Random rand = new Random();
            for (int i = 0; i < MAX_NUMBER_OF_OSTACLE; i++) {
            	Node n = new Node();
				n.setModel(model1);
				float xp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
				float zp = rand.nextFloat()*(LUNGH_MURO-LUNGH_OBST)*2-LUNGH_MURO+LUNGH_OBST;
				n.getRelativeTransform().setPosition(xp, ALTEZ_MURO, zp);
				n.getRelativeTransform().setMatrix(SFMatrix3f.getScale(LUNGH_OBST,ALTEZ_MURO,LUNGH_OBST));
				obstaclesNode.getSonNodes().add(n);
			}
            
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

            SFOGLSystemState.cleanupColorAndDepth(1, 1, 0, 1);

            //setup the View Projection
            float[] projection={
                    1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    0,0,0,1,
            };
            program.setupProjection(projection);

            //Change the Node transform
            if(t>0.5||t<0){
            	add = 0-add;
            }
            t+=add;
            float rotation=0.0f + t;
            float scaling=0.3f;
            
            SFMatrix3f matrix3f=SFMatrix3f.getScale(scaling,scaling,scaling);
            matrix3f=matrix3f.MultMatrix(SFMatrix3f.getRotationX(0.5f));
            matrix3f=matrix3f.MultMatrix(SFMatrix3f.getRotationY(rotation));
            matrix3f=matrix3f.MultMatrix(SFMatrix3f.getRotationZ(0));
            node.getRelativeTransform().setMatrix(matrix3f);
            node.updateTree(new SFTransform3f());

            //Draw the node
            node.draw();

            //int[] viewport=new int[4];
            //GLES20.glGetIntegerv(GLES20.GL_VIEWPORT,viewport,0);
            //Log.e("Graphics View Size", Arrays.toString(viewport));
        }
    }
}
