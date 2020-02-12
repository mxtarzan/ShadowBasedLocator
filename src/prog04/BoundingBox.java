package prog04;

import processing.core.PGraphics;

public class BoundingBox implements ApplicationConstants
{
	private float xmin_, xmax_, ymin_, ymax_;
	private int color_;
	
	/**	Creates a new bounding box at set dimensions and color.
	 * Note: Should verify that xmin ≤ xmax and ymin ≤ ymax
	 * 
	 * @param xmin	left bound of the box
	 * @param xmax	right bound of the box
	 * @param ymin	lower bound of the box
	 * @param ymax	upper bound of the box
	 * @param color	color of the box's contour
	 */
	public BoundingBox(float xmin, float xmax, float ymin, float ymax, int color)
	{
		xmin_ = xmin;
		xmax_ = xmax;
		ymin_ = ymin;
		ymax_ = ymax;
		color_ = color;
	}

	/**	Creates an empty bounding box with the specified contour color
	 * 
	 * @param color	color of the box's contour
	 */
	public BoundingBox(int color)
	{
		color_ = color;
	}

	/**	Creates an empty black box
	 * 
	 */
	public BoundingBox()
	{
		xmin_ = xmax_ = ymin_ = ymax_ = 0.f;
		color_ = 0xFF000000;
	}

	public void updatePosition(float xmin, float xmax, float ymin, float ymax)
	{
		xmin_ = xmin;
		xmax_ = xmax;
		ymin_ = ymin;
		ymax_ = ymax;
	}

	public float getXmin() 
	{
		return xmin_;
		
	}
	public float getXmax() 
	{
		return xmax_;
		
	}
	public float getYmin() 
	{
		return ymin_;
		
	}
	public float getYmax() 
	{
		return ymax_;
		
	}
	
	
	public boolean isInside(float x, float y)
	{		
		return ((x>=xmin_) && (x<=xmax_) && (y>=ymin_) && (y<=ymax_));
	}
	
	
	public void draw(PGraphics g)
	{
		g.strokeWeight(1.0f*PIXEL_TO_WORLD);
		g.stroke(color_);
		g.noFill();
		g.rect(xmin_, ymin_, xmax_-xmin_, ymax_-ymin_);
	}
}
