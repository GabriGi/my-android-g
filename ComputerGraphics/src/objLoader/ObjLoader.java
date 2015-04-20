package objLoader;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

import sfogl.integration.ArrayObject;
import sfogl.integration.Mesh;
import shadow.math.SFVertex3f;
import shadow.math.SFVertex4f;

/**
 * Created by Alessandro on 16/03/15.
 */
public class ObjLoader {

    public static ArrayObject[] arrayObjectFromFile(Context context,String filename){

        ObjArrays arrays=new ObjArrays();
        ObjFileIterator iterator=new ObjFileIterator(context, filename);

        while(iterator.hasNext()){
            String line=iterator.next().trim();
            arrays.takeCommandLine(line);
        }

        ArrayList<SFVertex4f> vertices=new ArrayList<SFVertex4f>();
        ArrayList<SFVertex4f> normals=new ArrayList<SFVertex4f>();
        ArrayList<SFVertex4f> txCoords=new ArrayList<SFVertex4f>();
        ArrayList<Integer> indices=new ArrayList<Integer>();

        ArrayList<ObjFace> indices__=arrays.getFaces();

        for (int i = 0; i < indices__.size(); i++) {
            ObjIndexSet[] set=indices__.get(i).getIndices();

            vertices.add(arrays.getVertices().get(set[0].getvIndex()));
            normals.add(arrays.getNormals().get(set[0].getVnIndex()));
            txCoords.add(arrays.getTxCoords().get(set[0].getVtIndex()));
            vertices.add(arrays.getVertices().get(set[1].getvIndex()));
            normals.add(arrays.getNormals().get(set[1].getVnIndex()));
            txCoords.add(arrays.getTxCoords().get(set[1].getVtIndex()));
            vertices.add(arrays.getVertices().get(set[2].getvIndex()));
            normals.add(arrays.getNormals().get(set[2].getVnIndex()));
            txCoords.add(arrays.getTxCoords().get(set[2].getVtIndex()));

            if(set.length==4){

                vertices.add(arrays.getVertices().get(set[0].getvIndex()));
                normals.add(arrays.getNormals().get(set[0].getVnIndex()));
                txCoords.add(arrays.getTxCoords().get(set[0].getVtIndex()));
                vertices.add(arrays.getVertices().get(set[2].getvIndex()));
                normals.add(arrays.getNormals().get(set[2].getVnIndex()));
                txCoords.add(arrays.getTxCoords().get(set[2].getVtIndex()));
                vertices.add(arrays.getVertices().get(set[3].getvIndex()));
                normals.add(arrays.getNormals().get(set[3].getVnIndex()));
                txCoords.add(arrays.getTxCoords().get(set[3].getVtIndex()));
            }
        }
        int size=vertices.size();
        for (int i = 0; i < size; i++) {
            indices.add(i);
        }

        ArrayObject[] objects=new ArrayObject[1];
        objects[0]=new ArrayObject(
                vListToFloats(vertices),
                vListToFloats(normals),
                vListToFloats(txCoords),
                iListToShorts(indices));
        //ArrayObject arrayObject= createModelFile("../MakeyourDodoJavaDev/building/models/models_oPalm01.sfb",
        //        "PalmModel01", groups01.get(3).getAsObject());

        return objects;
    }

    private static float[] vListToFloats(ArrayList<SFVertex4f> vertices_){

        float[] vertices=new float[3*vertices_.size()];
        for (int i = 0; i < vertices_.size(); i++) {
            SFVertex4f v=vertices_.get(i);
            vertices[3*i]=v.getX();
            vertices[3*i+1]=v.getY();
            vertices[3*i+2]=v.getZ();
        }
        return vertices;
    }

    private static short[] iListToShorts(ArrayList<Integer> indices_){

        short[] indices=new short[indices_.size()];
        for (int i = 0; i < indices_.size(); i++) {
            indices[i]=(short)(indices_.get(i)-0);
        }
        return indices;
    }

}
