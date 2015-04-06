package shadow.math;


/**
 * A value of 3 elements. A vector or a vertex (x,y). Add alternative, specific, more fast
 * methods 3f to the base nf method
 * 
 * @template Data_Structure
 * 
 * @author Alessandro Martinelli
 */
public class SFVertex3f extends SFValue{
	
	/**
	 * Return the distance between two vertices
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float getDistance(SFVertex3f v1,SFVertex3f v2) {
		float x=v1.getX()-v2.getX();
		float y=v1.getY()-v2.getY();
		float z=v1.getZ()-v2.getZ();
		return (float)(Math.sqrt(x*x+y*y+z*z));
	}
	
	/**
	 * Generate the middle Point between two poits A and B
	 * 
	 * @param A
	 *            the first Point
	 * @param B
	 *            the second Point
	 * @return the middle Point
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static SFVertex3f middle(SFVertex3f A,SFVertex3f B){
		return new SFVertex3f(
				(A.getV()[0]+B.getV()[0])*0.5f,(A.getV()[1]+B.getV()[1])*0.5f,(A.getV()[2]+B.getV()[2])*0.5f
		);
	}
	
	
	/**
	 * Create a new 3f value, assigning it (0,0)
	 * @param x
	 * @param y
	 */
	public SFVertex3f() {
		this(0,0,0);
	}

    public SFVertex3f(SFVertex4f value) {
        this(value.getV()[0],value.getV()[1],value.getV()[2]);
    }

	float[] v=new float[3];

	@Override
	public int getSize() {
		return 3;
	}
	
	@Override
	public float[] getV() {
		return v;
	}
	
	/**
	 */
	public SFVertex3f(float[] values) {
		//super(values);
		set3f(values[0],values[1],values[2]);
	}

	/**
	 * Create a new 3f value, assigning it
	 * @param x
	 * @param y
	 * @param z
	 */
	public SFVertex3f(double x, double y, double z) {
		//super(3);
		set3f((float)x,(float)y,(float)z);
	}

	/**
	 * Create a new 3f value, assigning it
	 * @param x
	 * @param y
	 * @param z
	 */
	public SFVertex3f(float x, float y, float z) {
		//super(3);
		set3f(x,y,z);
	}
	
	/**
	 * @deprecated please, create a new vertex than call set or set3f
	 * @param vertex
	 */
	public SFVertex3f(SFVertex3f vx) {
		//super(3);
		setArray(vx.getV());
	}
	
	/**
	 * Specific 3f version of the add method.
	 * @param vx
	 *            the constant to be added
	 */
	public void add3f(SFVertex3f vx){
		getV()[0]+=vx.getV()[0];
		getV()[1]+=vx.getV()[1];
		getV()[2]+=vx.getV()[2];
	}

	/**
	 * Specific 3f version of the addMult method.
	 * @param a
	 * @param vx
	 */
	public void addMult3f(float a,SFVertex3f vx){
		getV()[0]+=vx.getV()[0]*a;
		getV()[1]+=vx.getV()[1]*a;
		getV()[2]+=vx.getV()[2]*a;
	}
	
	/**
	 * @deprecated please, create a new vertex than call set or set2f
	 */
	public SFVertex3f cloneValue() {
		return new SFVertex3f(getV()[0],getV()[1],getV()[2]);
	}
	
	/**
	 * evaluate the cross vector between this vector and another one
	 * @param vx the other vector
	 * @return this x vx
	 */
	public SFVertex3f cross(SFVertex3f vx){
		return new SFVertex3f(getV()[1]*vx.getV()[2]-getV()[2]*vx.getV()[1],
							getV()[2]*vx.getV()[0]-getV()[0]*vx.getV()[2],
							getV()[0]*vx.getV()[1]-getV()[1]*vx.getV()[0]);
	}
	
	/**
	 * Specific 3f version of the dot method.
	 * @param vx
	 * @return
	 */
	public float dot3f(SFVertex3f vx){
		return vx.getV()[0]*getV()[0]+vx.getV()[1]*getV()[1]+vx.getV()[2]*getV()[2];
	}
	
	/**
	 * length of the Vector 
	 * @param vx
	 * @return
	 */
	public float getLength() {
		return (float)(Math.sqrt(getV()[0]*getV()[0]+getV()[1]*getV()[1]+getV()[2]*getV()[2]));
	}

	/**
	 * @return the x value
	 */
	public float getX(){
		return this.getV()[0];
	}
	
	/**
	 * @return the y value
	 */
	public float getY(){
		return this.getV()[1];
	}
	
	/**
	 * @return the z value
	 */
	public float getZ(){
		return getV()[2];
	}
	
	/**
	 * Specific 3f version of the mult method.
	 * @param m
	 */
	public void mult3f(float a){
		getV()[0]*=a;
		getV()[1]*=a;
		getV()[2]*=a;
	}
	
	/**
	 * Set this vector-vertex to be a unit vector with the same direction
	 * @param m
	 */
	public void normalize3f(){
		float lengthRec=1/getLength();
		getV()[0]*=lengthRec;
		getV()[1]*=lengthRec;
		getV()[2]*=lengthRec;
	}
	
	/**
	 * Scale this vector
	 * This.x=This.x*sx
	 * This.y=This.y*sy
	 * This.z=This.z*sz
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 */
	public void scale3f(float sx,float sy,float sz){
		getV()[0]*=sx;
		getV()[1]*=sy;
		getV()[2]*=sz;
	}
	
	/**
	 * Set the elements (x,y,z)
	 */
	public void set3f(float x,float y,float z){
		getV()[0]=x;
		getV()[1]=y;
		getV()[2]=z;
	}
	

	public void setX(float x){
		this.getV()[0]=x;
	}
	
	public void setY(float y){
		this.getV()[1]=y;
	}

	public void setZ(float z){
		getV()[2]=z;
	}
	
	/**
	 * Specific 3f version of the subtract method.
	 * @param vx
	 */
	public void subtract3f(SFVertex3f vx){
		getV()[0]-=vx.getV()[0];
		getV()[1]-=vx.getV()[1];
		getV()[2]-=vx.getV()[2];
	}
}
