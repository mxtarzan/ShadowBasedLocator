package prog04;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Ship extends GraphicObject implements ApplicationConstants, AnimatedObject{
	public static final int WHOLE_SHIP = 0;
	public static final int LEFT_TRIANGLE = 1;
	public static final int RIGHT_TRIANGLE = 2;
	public static final int SHIP_BODY = 3;
	public static final int NUM_OF_PARTS = 3;
	public static final int []PART_COLOR =  {	
												0xFFFF0000,	//	WHOLE ship
												//
												0xFF00FF00,	//	LEFT_triangle
												0xFFFFFF00,	//	RIGHT_triangle
												0xFF0000FF	//	body
											};
	
	public static final float SHIP_DIAMETER = 3;
	public static final float TRIANGLE_WIDTH = SHIP_DIAMETER/2f;
	public static final float TRIANGLE_HEIGHT = 1.25f*SHIP_DIAMETER;
	
	public static final float TRIANGLE_OFF_X = SHIP_DIAMETER/6;
	public static final float TRIANGLE_OFF_Y = -SHIP_DIAMETER/6;
	
	//										 p1,	p2,    p3		
	public static final float []LEFT_TRI =  {-TRIANGLE_OFF_X, TRIANGLE_OFF_Y, -TRIANGLE_OFF_X-TRIANGLE_WIDTH, TRIANGLE_OFF_Y, -TRIANGLE_OFF_X, TRIANGLE_OFF_Y +TRIANGLE_HEIGHT};
	public static final float []RIGHT_TRI =  {TRIANGLE_OFF_X, TRIANGLE_OFF_Y, TRIANGLE_OFF_X+TRIANGLE_WIDTH, TRIANGLE_OFF_Y, TRIANGLE_OFF_X, TRIANGLE_OFF_Y +TRIANGLE_HEIGHT};

	public static final float PIT_DIAMETER = SHIP_DIAMETER*2/3;
	public float health_;
	public int lives_;
	public int score_;
	
	public Ship() {
		super();
		vy_ = vx_ = angle_ = spin_ = 0;
		color_ = 0xFFFF0000;
		health_ = 100;
		lives_ = 3;
		score_ = 0;
		relativeBox_ = new BoundingBox[NUM_OF_PARTS+1];
		relativeBox_[LEFT_TRIANGLE] = new BoundingBox(LEFT_TRI[2], 	//	xmin
													  LEFT_TRI[0],	//	xmax
													  LEFT_TRI[5], 	//	ymin
													  LEFT_TRI[1], 	//	ymax
													  PART_COLOR[LEFT_TRIANGLE]);
		
		relativeBox_[RIGHT_TRIANGLE] = new BoundingBox(RIGHT_TRI[2], 	//	xmin
													  RIGHT_TRI[0],	//	xmax
												   	  RIGHT_TRI[5], 	//	ymin
													  RIGHT_TRI[1], 	//	ymax
													  PART_COLOR[RIGHT_TRIANGLE]);
		
		relativeBox_[SHIP_BODY] = new BoundingBox( -SHIP_DIAMETER/2, 	//	xmin
											  		SHIP_DIAMETER/2,		//	xmax
											  		-SHIP_DIAMETER/2, 	//	ymin
											  		SHIP_DIAMETER/2, 	//	ymax
											  		PART_COLOR[SHIP_BODY]);
		
		relativeBox_[WHOLE_SHIP] = new BoundingBox( -SHIP_DIAMETER/2, 	//	xmin of left ear
												  	SHIP_DIAMETER/2,	//	xmax of right ear
												  	-SHIP_DIAMETER/2, 				//	ymin of face
												  	RIGHT_TRI[5], 	//	ymax of either ear
												  	PART_COLOR[WHOLE_SHIP]);
		absoluteBox_ = new BoundingBox[NUM_OF_PARTS+1];
		for (int k=0; k<= NUM_OF_PARTS; k++){
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
		}
	}

	/**	renders the Face object.  At this point, we are already in the object's 
	 * reference frame.
	 * 
	 * @param app		reference to the sketch
	 */

	@Override
	protected void draw_(PGraphics g) {
		// we use this object's instance variable to access the application's instance methods and variables
				g.pushMatrix();
				//g.noStroke();
				g.fill(color_);
				g.ellipse(0, 0, SHIP_DIAMETER, SHIP_DIAMETER);
				g.popMatrix();

				g.pushMatrix();
				g.triangle(LEFT_TRI[0],LEFT_TRI[1], LEFT_TRI[2], LEFT_TRI[3], LEFT_TRI[4], LEFT_TRI[5]);
				g.popMatrix();
				
				g.pushMatrix();
				g.triangle(RIGHT_TRI[0],RIGHT_TRI[1], RIGHT_TRI[2], RIGHT_TRI[3], RIGHT_TRI[4], RIGHT_TRI[5]);
				g.popMatrix();	
				
				g.pushMatrix();
				g.strokeWeight(PIXEL_TO_WORLD*3);
				g.fill(0xFFFFFFFF);
				g.ellipse(0, 0, PIT_DIAMETER, PIT_DIAMETER);
				g.popMatrix();
	}

	@Override
	protected void updateAbsoluteBoxes_() {
		float sa = (float) Math.sin(angle_);
		float ca = (float) Math.cos(angle_);
		float x, y;
		absoluteBox_[SHIP_BODY].updatePosition((x_ - SHIP_DIAMETER/2),		// xmin
											    x_ + SHIP_DIAMETER/2,		//	xmax
											    y_ - SHIP_DIAMETER/2,		//	ymin
											    y_ + SHIP_DIAMETER/2);	//	ymax)
		x = relativeBox_[LEFT_TRIANGLE].getXmin();
		y = relativeBox_[LEFT_TRIANGLE].getYmin();
		float lp1x = x_ + x*ca - y *sa;
		float lp1y = y_ + y*ca + x*sa;
		x = relativeBox_[LEFT_TRIANGLE].getXmin();
		y = relativeBox_[LEFT_TRIANGLE].getYmax();
		float lp2x = x_ + x*ca - y *sa;
		float lp2y = y_ + y*ca + x*sa;
		x = relativeBox_[LEFT_TRIANGLE].getXmax();
		y = relativeBox_[LEFT_TRIANGLE].getYmin();
		float lp3x = x_ + x*ca - y *sa;
		float lp3y = y_ + y*ca + x*sa;
		x = relativeBox_[LEFT_TRIANGLE].getXmax();
		y = relativeBox_[LEFT_TRIANGLE].getYmax();
		float lp4x = x_ + x*ca - y *sa;
		float lp4y = y_ + y*ca + x*sa;
		float	[]cornerX1 = {	lp1x, lp2x, lp3x, lp4x};

		float	[]cornerY1 = {	lp1y, lp2y, lp3y, lp4y};	//	lower left
		
		absoluteBox_[LEFT_TRIANGLE].updatePosition(	PApplet.min(cornerX1),	//	xmin
													PApplet.max(cornerX1),	//	xmax
													PApplet.min(cornerY1),	//	ymin
													PApplet.max(cornerY1));	//	ymax
		
		x = relativeBox_[RIGHT_TRIANGLE].getXmin();
		y = relativeBox_[RIGHT_TRIANGLE].getYmin();
		float rp1x = x_ + x*ca - y *sa;
		float rp1y = y_ + y*ca + x*sa;
		x = relativeBox_[RIGHT_TRIANGLE].getXmin();
		y = relativeBox_[RIGHT_TRIANGLE].getYmax();
		float rp2x = x_ + x*ca - y *sa;
		float rp2y = y_ + y*ca + x*sa;
		x = relativeBox_[RIGHT_TRIANGLE].getXmax();
		y = relativeBox_[RIGHT_TRIANGLE].getYmin();
		float rp3x = x_ + x*ca - y *sa;
		float rp3y = y_ + y*ca + x*sa;
		x = relativeBox_[RIGHT_TRIANGLE].getXmax();
		y = relativeBox_[RIGHT_TRIANGLE].getYmax();
		float rp4x = x_ + x*ca - y *sa;
		float rp4y = y_ + y*ca + x*sa;
		float	[]cornerX2 = {	rp1x, rp2x, rp3x, rp4x};

		float	[]cornerY2 = {	rp1y, rp2y, rp3y, rp4y};	//	lower left
		
		absoluteBox_[RIGHT_TRIANGLE].updatePosition(PApplet.min(cornerX2),	//	xmin
													PApplet.max(cornerX2),	//	xmax
													PApplet.min(cornerY2),	//	ymin
													PApplet.max(cornerY2));	//	ymax
		
		absoluteBox_[WHOLE_SHIP].updatePosition(PApplet.min(absoluteBox_[LEFT_TRIANGLE].getXmin(),
															absoluteBox_[RIGHT_TRIANGLE].getXmin(),
															absoluteBox_[SHIP_BODY].getXmin()),	// xmin
												PApplet.max(absoluteBox_[LEFT_TRIANGLE].getXmax(),
															absoluteBox_[RIGHT_TRIANGLE].getXmax(),
															absoluteBox_[SHIP_BODY].getXmax()),	// xmax
												PApplet.min(absoluteBox_[LEFT_TRIANGLE].getYmin(),
															absoluteBox_[RIGHT_TRIANGLE].getYmin(),
															absoluteBox_[SHIP_BODY].getYmin()),	// ymin
												PApplet.max(absoluteBox_[LEFT_TRIANGLE].getYmax(),
															absoluteBox_[RIGHT_TRIANGLE].getYmax(),
															absoluteBox_[SHIP_BODY].getYmax()));	// xmax;

							}

	@Override
	public boolean isInside(float x, float y) {
		boolean inside = false;
		//	If the point is inside the global absolute bounding box
		if (absoluteBox_[WHOLE_SHIP].isInside(x, y))
		{
			//	test if the point is inside one of the sub-boxes
			//	Remember that Java (like C, C++, Python) uses lazy evaluation,
			//	so as soon as one test returns true, the evaluation ends.
			inside = absoluteBox_[LEFT_TRIANGLE].isInside(x, y) ||
					 absoluteBox_[RIGHT_TRIANGLE].isInside(x, y) ||
					 absoluteBox_[SHIP_BODY].isInside(x, y);
		}

		return inside;
	}

}
