package prog04;

import processing.core.PImage;

public class AnimatedBox extends Box implements AnimatedObject {
	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public AnimatedBox()
	{
		super();
	}

	/**	Constructor. Creates a random object at set location 
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 */
	public AnimatedBox(float x, float y)
	{
		super(x, y);
	}
	
	/**	Initializes all instance variables with random values, excect for the filling image
	 * @param image		The image to draw inside this box
	 * @param scale		scale to apply to the image
	 */
	public AnimatedBox(PImage image, float scale)
	{
		super(image, scale);
	}
	
	public AnimatedBox(PImage image, float scale, float x, float y)
	{
		super(image, scale);
		x_ = (float) (Math.random() + x);
		y_ = (float) (Math.random() + y);
	}

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
	public AnimatedBox(float x, float y, float angle, float width, float height, int color, float vx, 
					   float vy, float spin)
	{
		super(x, y, angle, width, height, color);
		vx_ = vx;
		vy_ = vy;
		spin_ = spin;
	}
	
	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param image		The image to draw inside this box
	 * @param vx		Horizontal component of the object's velocity vector (in world units per second)
	 * @param vy		Vertical component of the object's velocity vector (in world units per second)
	 * @param spin		Spin of the object (in rad/s)
	 */
	public AnimatedBox(float x, float y, float angle, float width, float height, PImage image, float vx, 
					   float vy, float spin)
	{
		super(x, y, angle, width, height, image);
		vx_ = vx;
		vy_ = vy;
		spin_ = spin;
	}
	
	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param scale		scale to apply to the image
	 * @param image		The image to draw inside this box
	 * @param vx		Horizontal component of the object's velocity vector (in world units per second)
	 * @param vy		Vertical component of the object's velocity vector (in world units per second)
	 * @param spin		Spin of the object (in rad/s)
	 */
	public AnimatedBox(float x, float y, float angle, float scale, PImage image, float vx, float vy, float spin)
	{
		super(x, y, angle, scale, image);
		vx_ = vx;
		vy_ = vy;
		spin_ = spin;
	}
}
