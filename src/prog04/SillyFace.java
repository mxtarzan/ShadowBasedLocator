package prog04;

import processing.core.PApplet;
import processing.core.PGraphics;

//NEW
//	The Face class is now derived from GraphicObject
/** Graphic class to draw a face.
 * 
 * @author jyh
 *
 */
public class SillyFace extends GraphicObject implements ApplicationConstants, AnimatedObject
{
	public static final int WHOLE_FACE = 0;
	public static final int LEFT_EAR = 1;
	public static final int RIGHT_EAR = 2;
	public static final int FACE = 3;
	public static final int NUM_OF_PARTS = 3;
	public static final int []PART_COLOR =  {	
												0xFFFF0000,	//	WHOLE SILLY FACE
												//
												0xFF00FF00,	//	LEFT_EAR
												0xFF00FF00,	//	RIGHT_EAR
												0xFF0000FF	//	FACE
											};
	
	public static final float FACE_DIAMETER = 5;
	public static final float EAR_DIAMETER = FACE_DIAMETER/2;
	public static final float EYE_OUTER_DIAMETER = FACE_DIAMETER/4;
	public static final float EYE_INNER_DIAMETER = FACE_DIAMETER/6;
	public static final float MOUTH_V_DIAMETER = FACE_DIAMETER/4; 
	public static final float MOUTH_H_DIAMETER = FACE_DIAMETER/2.5f; 
	//
	public static final float EAR_V_OFFSET = 0.3f*FACE_DIAMETER;
	public static final float EAR_H_OFFSET = 0.45f*FACE_DIAMETER;
	public static final float EYE_V_OFFSET = 0.2f*FACE_DIAMETER;
	public static final float EYE_H_OFFSET = 0.2f*FACE_DIAMETER;
	//
	private static final float LEFT_EAR_X = -EAR_H_OFFSET;
	private static final float LEFT_EAR_Y = EAR_V_OFFSET;
	private static final float RIGHT_EAR_X = EAR_H_OFFSET;
	private static final float RIGHT_EAR_Y = EAR_V_OFFSET;
	//
	private static final float LEFT_EYE_X = -EYE_H_OFFSET;
	private static final float LEFT_EYE_Y = EYE_V_OFFSET;
	private static final float RIGHT_EYE_X = EYE_H_OFFSET;
	private static final float RIGHT_EYE_Y = EYE_V_OFFSET;

	private static final float MOUTH_H_OFFSET = 0.f;
	private static final float MOUTH_V_OFFSET = -FACE_DIAMETER*0.1f;
	

	public SillyFace() {

		//NEW
		//	Our random initialization is now taken care of in the parent class's constructor.
		super();
		
		//	Create the relative bounding boxes
		relativeBox_ = new BoundingBox[NUM_OF_PARTS+1];
		relativeBox_[LEFT_EAR] = new BoundingBox(LEFT_EAR_X - EAR_DIAMETER/2, 	//	xmin
												 LEFT_EAR_X + EAR_DIAMETER/2,	//	xmax
												 LEFT_EAR_Y - EAR_DIAMETER/2, 	//	ymin
												 LEFT_EAR_Y + EAR_DIAMETER/2, 	//	ymax
												 PART_COLOR[LEFT_EAR]);
		relativeBox_[RIGHT_EAR] = new BoundingBox(RIGHT_EAR_X - EAR_DIAMETER/2, 	//	xmin
												  RIGHT_EAR_X + EAR_DIAMETER/2,		//	xmax
												  RIGHT_EAR_Y - EAR_DIAMETER/2, 	//	ymin
												  RIGHT_EAR_Y + EAR_DIAMETER/2, 	//	ymax
												  PART_COLOR[RIGHT_EAR]);
		relativeBox_[FACE] = new BoundingBox( -FACE_DIAMETER/2, 	//	xmin
											  +FACE_DIAMETER/2,		//	xmax
											  -FACE_DIAMETER/2, 	//	ymin
											  +FACE_DIAMETER/2, 	//	ymax
											  PART_COLOR[FACE]);
		relativeBox_[WHOLE_FACE] = new BoundingBox( LEFT_EAR_X - EAR_DIAMETER/2, 	//	xmin of left ear
												  	RIGHT_EAR_X + EAR_DIAMETER/2,	//	xmax of right ear
												  	-FACE_DIAMETER/2, 				//	ymin of face
												  	LEFT_EAR_Y + EAR_DIAMETER/2, 	//	ymax of either ear
												  	PART_COLOR[WHOLE_FACE]);
		
		//	create the absolute boxes
		absoluteBox_ = new BoundingBox[NUM_OF_PARTS+1];
		
		//NEW
		//	I was really bothered by the duplication of the fairly complex code for the
		//	absolute boxes (whenever you have multiple copies of long and complex code,
		//	there are chances that if you ever find a bug in that code, you will fix only
		//	one copy and forget to update the other(s).
		//	So here we first create the boxes, and then "update" their state.
		for (int k=0; k<= NUM_OF_PARTS; k++)
		{
			absoluteBox_[k] = new BoundingBox(PART_COLOR[k]);
		}
		updateAbsoluteBoxes_();
	}

	//NEW
	//	Here we run into our first true "class refactoring issue"
	//	The parent class GraphicalObject defines
	//		public abstract void draw(PApplet app);
	//	which we must implement.
	//	On the other hand, the Face class we implemented together has a method
	//		public void draw(BoundingBoxMode mode)
	//	Adding the PApplet reference to argument is not a problem.  On the other
	//	hand, the fact that the parent class is BoundingBox-unaware is.
	//	Eventually, bounding box support should be folded into the parent class,
	//	but this requires more thought.  This revision is just a cleanup.  
	//	Fortunately, the box-drawing code can be fairly easily separated from the
	//	So, we are going to have a Face-specific draw method that invokes the 
	//	generic draw of the parent class, then draws the boxes.
	/**	renders the Face object
	 * 
	 * @param app		reference to the sketch
	 * @param theMode	should the object be drawn with a bounding box?
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode)
	{
		//NEW
		//	Invokes method declared in the parent class, that draws the object
		draw_(g);
		
		//	Then draw the boxes, if needed

		if (boxMode == BoundingBoxMode.RELATIVE_BOX)
		{
			// we use this object's instance variable to access the application's instance methods and variables
			g.pushMatrix();

			g.translate(x_,  y_);
			g.rotate(angle_);		

			for (BoundingBox box : relativeBox_)
				box.draw(g);
			
			g.popMatrix();	
		}
		
		else if (boxMode == BoundingBoxMode.ABSOLUTE_BOX)
		{
			for (BoundingBox box : absoluteBox_)
				if (box != null)
					box.draw(g);
			
		}
	}


	public void drawAllQuadrants(PGraphics g, BoundingBoxMode boxMode)
	{
		draw(g, boxMode);
		
		if (shouldGetDrawnInQuadrant_[NORTH_WEST])
		{
			g.pushMatrix();
			g.translate(XMIN-XMAX, YMIN-YMAX);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[NORTH])
		{
			g.pushMatrix();
			g.translate(0, YMIN-YMAX);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[NORTH_EAST])
		{
			g.pushMatrix();
			g.translate(XMAX-XMIN, YMIN-YMAX);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[EAST])
		{
			g.pushMatrix();
			g.translate(XMAX-XMIN, 0);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[SOUTH_EAST])
		{
			g.pushMatrix();
			g.translate(XMAX-XMIN, YMAX-YMIN);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[SOUTH])
		{
			g.pushMatrix();
			g.translate(0, YMAX-YMIN);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[SOUTH_WEST])
		{
			g.pushMatrix();
			g.translate(XMIN-XMAX, YMAX-YMIN);
			draw(g, boxMode);
			g.popMatrix();
		}
		if (shouldGetDrawnInQuadrant_[WEST])
		{
			g.pushMatrix();
			g.translate(XMIN-XMAX, 0);
			draw(g, boxMode);
			g.popMatrix();
		}
	}

	/**	renders the Face object
	 * 
	 * @param app		reference to the sketch
	 * @param theMode	should the object be drawn with a bounding box?
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode, int quad)
	{
		if (quad >= NORTH && quad <= NORTH_EAST && shouldGetDrawnInQuadrant_[quad])
		{
			//	Invokes method declared in the parent class, that draws the object
			draw_(g);

			//	Then draw the boxes, if needed

			if (boxMode == BoundingBoxMode.RELATIVE_BOX)
			{
				// we use this object's instance variable to access the application's instance methods and variables
				g.pushMatrix();

				g.translate(x_,  y_);
				g.rotate(angle_);		

				for (BoundingBox box : relativeBox_)
					box.draw(g);

				g.popMatrix();	
			}

			else if (boxMode == BoundingBoxMode.ABSOLUTE_BOX)
			{
				for (BoundingBox box : absoluteBox_)
					if (box != null)
						box.draw(g);

			}

			g.pushMatrix();	
			if (mustDrawReferenceFrame_)
				drawReferenceFrame(g);
			g.popMatrix();
		}
	}

	/**	renders the Face object.  At this point, we are already in the object's 
	 * reference frame.
	 * 
	 * @param app		reference to the sketch
	 */
	public void draw_(PGraphics g)
	{
		// we use this object's instance variable to access the application's instance methods and variables
		g.pushMatrix();

		g.noStroke();
		g.fill(color_);
		g.ellipse(0, 0, FACE_DIAMETER, FACE_DIAMETER);

		//	draw the left ear
		g.pushMatrix();
		g.noStroke();
		g.fill(color_);
		g.translate(LEFT_EAR_X, LEFT_EAR_Y);
		g.ellipse(0, 0, EAR_DIAMETER, EAR_DIAMETER);
		g.popMatrix();  

		//	draw the right ear
		g.pushMatrix();
		g.noStroke();
		g.fill(color_);
		g.translate(RIGHT_EAR_X, RIGHT_EAR_Y);
		g.ellipse(0, 0, EAR_DIAMETER, EAR_DIAMETER);
		g.popMatrix();  

		//	draw the left and right eyes (I could have gone the Push&pop way here as
		//	well, and would if the eyes were more complex, but here they are simply
		//	ellipses, so no need to make it over-complex.
		g.fill(255);
		g.ellipse(LEFT_EYE_X, LEFT_EYE_Y, EYE_OUTER_DIAMETER, EYE_OUTER_DIAMETER);
		g.ellipse(RIGHT_EYE_X, RIGHT_EYE_Y, EYE_OUTER_DIAMETER, EYE_OUTER_DIAMETER);
		g.fill(0);
		g.ellipse(LEFT_EYE_X, LEFT_EYE_Y, EYE_INNER_DIAMETER, EYE_INNER_DIAMETER);
		g.ellipse(RIGHT_EYE_X, RIGHT_EYE_Y, EYE_INNER_DIAMETER, EYE_INNER_DIAMETER);

		// draw the mouth
		g.stroke(0);
		g.noFill();
		g.strokeWeight(2*PIXEL_TO_WORLD);
		g.arc(MOUTH_H_OFFSET, MOUTH_V_OFFSET, 
				MOUTH_H_DIAMETER, MOUTH_H_DIAMETER, -7*PApplet.PI/8, -PApplet.PI/8);
		
		g.popMatrix();	
	}

	/**
	 * 	Computes the new dimensions of the object's absolute bounding boxes, for
	 * the object's current position and orientation.
	 */
	protected void updateAbsoluteBoxes_()
	{
		float cA = PApplet.cos(angle_), sA = PApplet.sin(angle_);
		float 	centerLeftEarX = x_ + cA*LEFT_EAR_X - sA*LEFT_EAR_Y,
				centerLeftEarY = y_ + cA*LEFT_EAR_Y + sA*LEFT_EAR_X,
				centerRightEarX = x_ + cA*RIGHT_EAR_X - sA*RIGHT_EAR_Y,
				centerRightEarY = y_ + cA*RIGHT_EAR_Y + sA*RIGHT_EAR_X;
				
		
		absoluteBox_[LEFT_EAR].updatePosition(centerLeftEarX - EAR_DIAMETER/2,	//	xmin
											  centerLeftEarX + EAR_DIAMETER/2,	//	xmax
											  centerLeftEarY - EAR_DIAMETER/2,	//	ymin
											  centerLeftEarY + EAR_DIAMETER/2);	//	ymax
		absoluteBox_[RIGHT_EAR].updatePosition(centerRightEarX - EAR_DIAMETER/2,	//	xmin
											   centerRightEarX + EAR_DIAMETER/2,	//	xmax
											   centerRightEarY - EAR_DIAMETER/2,	//	ymin
											   centerRightEarY + EAR_DIAMETER/2);	//	ymax
		absoluteBox_[FACE].updatePosition(x_ - FACE_DIAMETER/2,		// xmin
										  x_ + FACE_DIAMETER/2,		//	xmax
										  y_ - FACE_DIAMETER/2,		//	ymin
										  y_ + FACE_DIAMETER/2);	//	ymax)

		absoluteBox_[WHOLE_FACE].updatePosition(PApplet.min(absoluteBox_[LEFT_EAR].getXmin(),
														 absoluteBox_[RIGHT_EAR].getXmin(),
														 absoluteBox_[FACE].getXmin()),	// xmin
												PApplet.max(absoluteBox_[LEFT_EAR].getXmax(),
															absoluteBox_[RIGHT_EAR].getXmax(),
															absoluteBox_[FACE].getXmax()),	// xmax
												PApplet.min(absoluteBox_[LEFT_EAR].getYmin(),
															absoluteBox_[RIGHT_EAR].getYmin(),
															absoluteBox_[FACE].getYmin()),	// ymin
												PApplet.max(absoluteBox_[LEFT_EAR].getYmax(),
															absoluteBox_[RIGHT_EAR].getYmax(),
															absoluteBox_[FACE].getYmax()));	// xmax;
		
	}

	//NEW
	//	Food for thought for next revision:   Should the method *do* something,
	//	store something, or return something (beyond a boolean)?
	//		- do something:  This would imply that there is only a single context
	//			of "is inside" test, e.g. in a shooter game, but even this type 
	//			of games have objects that we way need to interact with in non-destructive
	//			contexts, e.g. a button on a game object, or a door handle.
	//		- store something, for example a reference to the subpart that returned
	//			true (for example, objectSelected, or objectShot.  The app could
	//			presumably make another call later a specific method to indicate to
	//			indicate what to (try to) do to the object.  The problem then is how 
	//			that reference would be reset, and after much time.
	//		- return something (an index, 0 or -1 for no-hit, or a reference to the 
	//			part hit, null if no-hit).  The caller could then call another 
	//			method of this class to indicate what to (try to) do to/with the object.
	//	Personally, I prefer the last solution, particularly if the class has very few
	//	to no public methods that modify the state of the object.
	//
	/**	Performs a hierarchical search to determine whether the point received
	 * as parameter is inside the face.  OK, it's a pretty simply hierarchy, being
	 * only one level deep, but it gives us a chance to think about the problem.
	 * 
	 * @param x		x coordinate of a point in the world reference frame
	 * @param y		y coordinate of a point in the world reference frame
	 * @return	true if the point at (x, y) lies inside this face object.
	 */
	public boolean isInside(float x, float y)
	{
		boolean inside = false;
		//	If the point is inside the global absolute bounding box
		if (absoluteBox_[WHOLE_FACE].isInside(x, y))
		{
			//	test if the point is inside one of the sub-boxes
			//	Remember that Java (like C, C++, Python) uses lazy evaluation,
			//	so as soon as one test returns true, the evaluation ends.
			inside = absoluteBox_[LEFT_EAR].isInside(x, y) ||
					 absoluteBox_[LEFT_EAR].isInside(x, y) ||
					 absoluteBox_[FACE].isInside(x, y);
		}

		return inside;
	}
}