package prog04;

public class AnimatedEllipse extends Ellipse implements AnimatedObject
{
	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param color		The color is stored as a single int in hex format
	 * @param vx		Horizontal component of the object's velocity vector (in world units per second)
	 * @param vy		Vertical component of the object's velocity vector (in world units per second)
	 * @param spin		Spin of the object (in rad/s)
	 */
	public AnimatedEllipse(float x, float y, float angle, float width, float height, int color, float vx, 
						   float vy, float spin)
	{
		super(x, y, angle, width, height, color);
		vx_ = vx;
		vy_ = vy;
		spin_ = spin;
	}
	
	/**	Constructor. Creates a random object at set location 
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 */
	public AnimatedEllipse(float x, float y)
	{
		super(x, y);
	}
	
	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public AnimatedEllipse()
	{
		super();
	}

}
