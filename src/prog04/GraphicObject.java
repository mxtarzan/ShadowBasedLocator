package prog04;

import java.awt.geom.Point2D;

import processing.core.*; 

/**	This abstract class is the parent class for all classes of graphic objects.  
 *  It stores all the instance variable describing the position-dimension-color-motion
 *  attributes of a graphic object.  It defines that abstract draw and update methods that 
 * 	its subclasses must implement
 * 
 * @author jyh
 *
 */
public abstract class GraphicObject implements ApplicationConstants
{
	//-------------------------------------
	//	Class constants
	//-------------------------------------

	/**	Minimum width of an object
	 * 
	 */
	public final static float MIN_WIDTH = WORLD_WIDTH/20;
	
	/**	Maximum width of an object
	 * 
	 */
	public final static float MAX_WIDTH = WORLD_WIDTH/6;
	
	/**	Minimum height of an object
	 * 
	 */
	public final static float MIN_HEIGHT = WORLD_HEIGHT/20;
	
	/**	Maximum height of an object
	 * 
	 */
	public final static float MAX_HEIGHT = WORLD_HEIGHT/6;
	
	/**	Minimum speed of an object
	 * 
	 */
	final static float MIN_SPEED = WORLD_WIDTH/20;
	
	/**	Maximum speed of an object
	 * 
	 */
	public final static float MAX_SPEED = WORLD_WIDTH/8;
	
	/**	Minimum (unsigned) spin of an object
	 * 
	 */
	public final static float MIN_SPIN = PApplet.PI/12;
	
	/**	Maximum (unsigned) spin of an object
	 * 
	 */
	public final static float MAX_SPIN = PApplet.PI;
	

	/**	Indicates whether graphic objects should draw their reference frame
	 */
	protected static boolean mustDrawReferenceFrame_ = false;
	
	/**	Indicates what bounding box graphic objects should render.
	 */
	private static BoundingBoxMode boxMode_;
	
	/**	How to treat the edges of the window
	 */
	private static AnimationMode animationMode_ = AnimationMode.BOX_WORLD;

	
	//-------------------------------------
	//	Instance variables
	//	As explained in an earlier version, in an ideal OOP implementation we would want all the instance
	//	variables to be private and only be accessible through setter and getter methods.
	//	I generally want you to develop good code, but here I feel that all the added code would detract from the 
	//	main purpose of the course, which is animation.  So, I declare all my instance variables "protected"
	//	in the parent class.
	//-------------------------------------

	/**	x coordinate of the object's center (in world coordinates)
	 * 
	 */
	protected float x_;
	
	/**	y coordinate of the object's center (in world coordinates)
	 * 
	 */
	protected float y_;
	
	/**	Orientation of the object (in rad)
	 * 
	 */
	protected float angle_;
	
	/**	width of the object (in world units)
	 * 
	 */
	protected float width_;
	
	/**	height of the object (in world units)
	 * 
	 */
	protected float height_;
		
	/**	Horizontal component of the object's velocity vector (in world units per second)
	 * 
	 */
	protected float vx_;
	
	/**	Vertical component of the object's velocity vector (in world units per second)
	 * 
	 */
	protected float vy_;
	

	/**	Spin of the object (in rad/s)
	 * 
	 */
	protected float spin_;
	
	/**	The color is stored as a single int in hex format
	 * 
	 */
	int color_;
	
	/**	The time since ceation	 * 
	 */
	float timealive_;
	
	/**	Every GraphicObject must store and update at least one
	 * 	absolute bounding box.  
	 * 	The bounding box at index 0 should be the global bounding box of the object.
	 */
	protected BoundingBox []absoluteBox_;

	/**	Every GraphicObject must store and update at least one
	 * 	relative bounding box
	 * 	The bounding box at index 0 should be the global bounding box of the object.
	 */
	protected BoundingBox []relativeBox_;

	/**	Used in a cylindrical or toroidal world:  Indicates in which of
	 * the (possibly) eight adjacent quadrants the object should get drawn
	 */
	protected boolean []shouldGetDrawnInQuadrant_;
	

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
	public GraphicObject(float x, float y, float angle, float width, float height, 
						   int color, float vx, float vy, float spin)
	{
		x_ = x;
		y_ = y;
		angle_ = angle;
		width_ = width;
		height_ = height;
		color_ = color;
		vx_ = vx;
		vy_ = vy;
		spin_ = spin;
		shouldGetDrawnInQuadrant_ = new boolean[8];
	}

	/**	Constructor for static objects. Initializes all non-dynamic instance variables 
	 *  to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param color		The color is stored as a single int in hex format
	 */
	public GraphicObject(float x, float y, float angle, float width, float height, 
						   int color)
	{
		x_ = x;
		y_ = y;
		angle_ = angle;
		width_ = width;
		height_ = height;
		color_ = color;
		vx_ = vy_ = spin_ = 0.f;
		shouldGetDrawnInQuadrant_ = new boolean[8];
	}

	/**	Constructor. Creates a random object at set location 
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 */
	public GraphicObject(float x, float y)
	{
		x_ = x;
		y_ = y;
		angle_ = (float)(Math.random()*2*Math.PI);
		width_ = (float) (Math.random()*(MAX_WIDTH-MIN_WIDTH) + MIN_WIDTH);;
		height_ = (float) (Math.random()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);
		color_ = (0xFF000000 | ((randomByte_() << 16) & 0x00FF0000)  
	   	 					 | ((randomByte_() << 8) & 0x0000FF00) 
	   	 					 | (randomByte_() & 0xFF0000FF));
		vx_ = vy_ = spin_ = 0.f;
		shouldGetDrawnInQuadrant_ = new boolean[8];
	}
	

	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public GraphicObject()
	{
		x_ = (float) (Math.random()*(XMAX-XMIN) + XMIN);
		y_ = (float) (Math.random()*(YMAX-YMIN) + YMIN);
		angle_ = (float)(Math.random()*0.5*Math.PI);
		width_ = (float) (Math.random()*(MAX_WIDTH-MIN_WIDTH) + MIN_WIDTH);;
		height_ = (float) (Math.random()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);
//		color_ = (0xFF000000 | ((randomByte_() << 16) & 0x00FF0000)  
//						   	 | ((randomByte_() << 8) & 0x0000FF00) 
//						   	 | (randomByte_() & 0xFF0000FF));
		color_ = 0xFF000000 | (int)(Math.random() * Integer.MAX_VALUE);
		generateRandomMovement_();
		shouldGetDrawnInQuadrant_ = new boolean[8];
	}

	/**	Renders the object in all quadrants in which it is visible.
	 * 
	 * @param app		reference to the main app
	 */
	public void drawAllQuadrants(PGraphics g)
	{
		draw(g);
		
		if (shouldGetDrawnInQuadrant_[NORTH_WEST])
		{
			g.pushMatrix();
			g.translate(XMIN-XMAX, YMIN-YMAX);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[NORTH])
		{
			g.pushMatrix();
			g.translate(0, YMIN-YMAX);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[NORTH_EAST])
		{
			g.pushMatrix();
			g.translate(XMAX-XMIN, YMIN-YMAX);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[EAST])
		{
			g.pushMatrix();
			g.translate(XMAX-XMIN, 0);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[SOUTH_EAST])
		{
			g.pushMatrix();
			g.translate(XMAX-XMIN, YMAX-YMIN);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[SOUTH])
		{
			g.pushMatrix();
			g.translate(0, YMAX-YMIN);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[SOUTH_WEST])
		{
			g.pushMatrix();
			g.translate(XMIN-XMAX, YMAX-YMIN);
			draw(g);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[WEST])
		{
			g.pushMatrix();
			g.translate(XMIN-XMAX, 0);
			draw(g);
			g.popMatrix();
		}
	}

	/**	Renders this object.
	 * 
	 * @param app	The Processing application in which the action takes place
	 */
	public void draw(PGraphics g)
	{
		g.pushMatrix();
		
		g.translate(x_, y_);
		g.rotate(angle_);
				
		g.fill(color_);
		g.stroke(0);
		g.strokeWeight(1.0f*DRAW_IN_PIXELS_SCALE);		
		//
		g.pushMatrix();
		draw_(g);
		
		//	render the relative boxes if needed
		if (boxMode_ == BoundingBoxMode.RELATIVE_BOX)
		{
			//	draw the boxes by decreasing index order, so that the global box gets displayed last
			for (int k = relativeBox_.length-1; k>=0; k--)
			{
				relativeBox_[k].draw(g);
			}
		}
		g.popMatrix();

		if (mustDrawReferenceFrame_)
			drawReferenceFrame(g);
		g.popMatrix();
		
		//	Now that we are back in the world reference frame, draw the absolute boxes
		//	if needed
		if (boxMode_ == BoundingBoxMode.ABSOLUTE_BOX)
		{
			//	draw the boxes by decreasing index order, so that the global box gets displayed last
			for (int k = absoluteBox_.length-1; k>=0; k--)
			{
				absoluteBox_[k].draw(g);
			}
		}
	}

	/**	Declare the method that subclasses must implement.
	 * When this method is called, we are already in the reference frame of the
	 * object, so the implementation of this method should not invoke translate/rotate
	 * 
	 * @param app	The Processing application in which the action takes place
	 */
	protected abstract void draw_(PGraphics app);
	
	
	/**
	 * 	Computes the new dimensions of the object's absolute bounding boxes, for
	 * the object's current position and orientation.
	 */
	protected abstract void updateAbsoluteBoxes_();

	
	/**	The more complex animation method, where wee need to handle
	 * "hit a window edge" situations.  Must be implemented by subclasses.
	 * This method will only be called for instances of classes that
	 * implement AnimatedObject.
	 * 
	 * @param dt				time elapsed since last invocation
	 */
	public void update(float dt)
	{
		
		//	First, compute the values 
		switch (animationMode_)
		{
			case BOX_WORLD:
				// we use this object's instance variable to access the application's instance methods and variables
				if (absoluteBox_[0].getXmax() >= XMAX)
				{
					x_ -= (absoluteBox_[0].getXmax() - XMAX);
					vx_ = -vx_;
				}
				else if (absoluteBox_[0].getXmin() <= XMIN)
				{
					x_ += (XMIN - absoluteBox_[0].getXmin());
					vx_ = -vx_;
				}
				if (absoluteBox_[0].getYmax() >= YMAX)
				{
					y_ -= (absoluteBox_[0].getYmax() - YMAX);
					vy_ = -vy_;
				}
				else if (absoluteBox_[0].getYmin() <= YMIN)
				{
					y_ += (YMIN - absoluteBox_[0].getYmin());
					vy_ = -vy_;
				}
				break;
				
			case CYLINDER_WORLD:
			{			
				// we use this object's instance variable to access the application's instance methods and variables
				if (absoluteBox_[0].getXmax() >= XMAX)
				{
					shouldGetDrawnInQuadrant_[WEST] = true;
	
					// we use this object's instance variable to access the application's instance methods and variables
					if (absoluteBox_[0].getXmin() >= XMAX)
					{
						x_ -= XMAX - XMIN;
					}
				}
				else 
				{
					shouldGetDrawnInQuadrant_[WEST] = false;
	
					if (absoluteBox_[0].getXmin() <= XMIN)
					{
						shouldGetDrawnInQuadrant_[EAST] = true;
	
						if (absoluteBox_[0].getXmax() <= XMIN)
						{
							x_ += XMAX - XMIN;
						}
					}
					else
					{
						shouldGetDrawnInQuadrant_[EAST] = false;			
					}
				}
				if (absoluteBox_[0].getYmax() >= YMAX)
				{
					y_ -= (absoluteBox_[0].getYmax() - YMAX);
					vy_ *= -1;
				}
				else if (absoluteBox_[0].getYmin() <= YMIN)
				{
					y_ += (YMIN - absoluteBox_[0].getYmin());
					vy_ *= -1;
				}
			}
			break;
			
			case SPHERE_WORLD:
			{			
				// we use this object's instance variable to access the application's instance methods and variables
				if (absoluteBox_[0].getXmax() >= XMAX)
				{
					shouldGetDrawnInQuadrant_[WEST] = true;
	
					// we use this object's instance variable to access the application's instance methods and variables
					if (absoluteBox_[0].getXmin() >= XMAX)
					{
						x_ -= XMAX - XMIN;
					}
				}
				else 
				{
					shouldGetDrawnInQuadrant_[WEST] = false;
	
					if (absoluteBox_[0].getXmin() <= XMIN)
					{
						shouldGetDrawnInQuadrant_[EAST] = true;
	
						if (absoluteBox_[0].getXmax() <= XMIN)
						{
							x_ += XMAX - XMIN;
						}
					}
					else
					{
						shouldGetDrawnInQuadrant_[EAST] = false;			
					}
				}
				if (absoluteBox_[0].getYmax() >= YMAX)
				{
					shouldGetDrawnInQuadrant_[NORTH] = true;
					
					// we use this object's instance variable to access the application's instance methods and variables
					if (absoluteBox_[0].getYmin() >= YMAX)
					{
						y_ -= YMAX - YMIN;
					}
				}
				else if (absoluteBox_[0].getYmin() <= YMIN)
				{
					shouldGetDrawnInQuadrant_[NORTH] = false;
					
					if (absoluteBox_[0].getYmin() <= YMIN)
					{
						shouldGetDrawnInQuadrant_[SOUTH] = true;
	
						if (absoluteBox_[0].getYmax() <= YMIN)
						{
							y_ += YMAX - YMIN;
						}
					}
					else
					{
						shouldGetDrawnInQuadrant_[SOUTH] = false;			
					}
				}
			}
			break;
			case TORUS_WORLD:
				break;

			case WINDOW_WORLD:
				break;
		}
		
		//	Now we have the correct version of the update method, in which the 
		//	on-screen speed of object does not depend on the CPU's performance.
		x_ += vx_*dt;
		y_ += vy_*dt;
		angle_ += spin_*dt;
		timealive_ += dt;

		//	and we invoke our new method to update the absolute boxes
		updateAbsoluteBoxes_();
	}

	/**	Getter method for the quadrant visibility array 
	 * 
	 * @param quad	the quadrant to check
	 * @return	whether this object is visible in the quadrant specified
	 */
	public boolean isVisibleInQuadrant(int quad)
	{
		return quad >= NORTH && quad <= NORTH_EAST && shouldGetDrawnInQuadrant_[quad];	
	}

	/**	Utility random generator 
	 * 
	 * @return	random unsigned byte value [0-255]
	 */
	static byte randomByte_()
	{
		return (byte)(256*Math.random());
	}
	
	/**	Method to be implemented by subclasses, to determine whether a world point is
	 * inside the object
	 * 
	 * @param x	x coordinate of the point
	 * @param y	y coordinate of the point
	 * @return	true if the point (x, y) is inside this object.
	 */
	public abstract boolean isInside(float x, float y);

	/**	Method to invoke determine whether a world point is
	 * inside the object
	 * 
	 * @param pt the point for which to perform the test
	 * @return	true if the point (x, y) is inside this object.
	 */
	public boolean isInside(Point2D.Float pt)
	{
		return isInside(pt.x, pt.y);
	}

	public static void drawReferenceFrame(PGraphics g)
	{
		g.strokeWeight(PIXEL_TO_WORLD);
		g.stroke(255, 0, 0);
		g.line(0, 0, WORLD_WIDTH/20, 0);
		g.stroke(0, 255, 0);
		g.line(0, 0, 0, WORLD_WIDTH/20);
	}

	/**	Called by the PApplet to tell whether graphic objects should 
	 * draw their reference frame (for debugging/testing purposes)
	 * @param mustDrawFrame	true if object should draw their reference frame
	 */
	public static void setDrafReferenceFrame(boolean mustDrawFrame)
	{
		mustDrawReferenceFrame_ = mustDrawFrame;
	}

	/**	Called by the PApplet to tell whether graphic objects should 
	 * draw their reference frame (for debugging/testing purposes)
	 * @param mustDrawFrame	true if object should draw their reference frame
	 */
	public static void setBoundingBoxMode(BoundingBoxMode mode)
	{
		boxMode_ = mode;
	}

	/**	Called by the PApplet to tell whether graphic objects should 
	 * draw their reference frame (for debugging/testing purposes)
	 * @param mustDrawFrame	true if object should draw their reference frame
	 */
	public static void setAnimationMode(AnimationMode mode)
	{
		animationMode_ = mode;
	}
	
	/**	Creates a default single bounding box setup
	 */ 
	protected void setupDefaultBoundingBoxes_()
	{		
		absoluteBox_ = new BoundingBox[1];
		absoluteBox_[0] = new BoundingBox(0xFFFF0000);
		updateAbsoluteBoxes_();
		//
		relativeBox_ = new BoundingBox[1];
		relativeBox_[0] = new BoundingBox(-width_/2, width_/2, -height_/2, +height_/2, 0xFFFF0000);		
	}
	
	protected void generateRandomMovement_()
	{
		double heading = 2*Math.PI*Math.random();
		double v = (float) (Math.random()*(MAX_SPEED-MIN_SPEED) + MIN_SPEED);
		vx_ = (float)(v * Math.cos(heading));
		vy_ = (float)(v * Math.sin(heading));
		spin_ = (float) (Math.random()*(MAX_SPIN-MIN_SPIN) + MIN_SPIN);;
	}
}
