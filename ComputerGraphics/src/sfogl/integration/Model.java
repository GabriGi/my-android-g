package sfogl.integration;

/**
 * Geometry + Material
 * 
 * @author Alessandro
 */
public class Model {

	private Material material;
	private Mesh geometry=null;
	
    public void setMaterialComponent(Material material) {
		this.material = material;
	}
	
	public void setRootGeometry(Mesh geometry) {
		this.geometry = geometry;
	}
	
	public void draw(){
        material.getProgram().getShader().apply();
        material.loadData();
        geometry.draw(material.getProgram().getShader());
	}
	
	public Mesh getGeometry() {
		return geometry;
	}
	
	public Material getMaterial() {
		return material;
	}

    public ShadingProgram getProgram(){
        return material.getProgram();
    }

    public float[] getMinAndMaxValues() {
    	return geometry.getMinAndMaxValues();
	}
    
    public float[] getScaleAndMiddleValues() {
    	return geometry.getScaleAndMiddleValues();
	}
}
