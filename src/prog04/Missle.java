package prog04;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Missle extends GraphicObject implements ApplicationConstants, AnimatedObject{
	
	public Missle(float x, float y, float vx, float vy){
			x_ = x;
			y_ = y;
			angle_ = (float)(Math.random()*2*Math.PI);
			height_ = MIN_WIDTH/2;
			width_ = MIN_WIDTH/2;
			vx_ = vx;
			vy_ = vy;
			color_ = 0xFFFF0000;/*(0xFF000000 | ((randomByte_() << 16) & 0x00FF0000)  
	 					 | ((randomByte_() << 8) & 0x0000FF00) 
	 					 | (randomByte_() & 0xFF0000FF));*/
			spin_ = 0.f;
			shouldGetDrawnInQuadrant_ = new boolean[8];
			setupDefaultBoundingBoxes_();

}
	@Override
	protected void draw_(PGraphics app) {
		app.pushMatrix();
	    app.translate(-width_/4,  -height_/4);
	    app.rect(0,  0,  width_/2, height_/2);
		app.popMatrix();
		//app.point(x_,y_);
	}    

	@Override
	protected void updateAbsoluteBoxes_() {
//		could definitely be optimized
			float cA = PApplet.cos(angle_), sA = PApplet.sin(angle_);
			float hwidth = width_/2, hheight = height_/2;
			float dx = Math.max(Math.abs(cA*hwidth - sA*hheight),
								Math.abs(cA*hwidth + sA*hheight)),
				  dy = Math.max(Math.abs(sA*hwidth + cA*hheight),
						  		Math.abs(sA*hwidth -  cA*hheight));
			
			float	[]cornerX = {	x_ - dx,	//	upper left
									x_ + dx,	//	upper right
									x_ + dx,	//	lower right
									x_ - dx};	//	lower left
			
			float	[]cornerY = {	y_ - dy,	//	upper left
									y_ + dy,	//	upper right
									y_ + dy,	//	lower right
									y_ - dy};	//	lower left
					
			absoluteBox_[0].updatePosition(	PApplet.min(cornerX),	//	xmin
											PApplet.max(cornerX),	//	xmax
											PApplet.min(cornerY),	//	ymin
											PApplet.max(cornerY));	//	ymax
	}

	@Override
	public boolean isInside(float x, float y) {
////		Convert x and y into this object's reference frame coordinates
//			double cosAngle = Math.cos(angle_), sinAngle = Math.sin(angle_);
//			double xb = cosAngle*(x - x_) + sinAngle*(y - y_);
//			double yb = cosAngle*(y - y_) + sinAngle*(x_ - x);
//			return ((PApplet.abs((float) xb) <= width_/2) && (PApplet.abs((float) yb) <= height_/2));
		return (x>=x_-MIN_WIDTH && x<=x_+MIN_WIDTH && y>=y_-MIN_WIDTH && y<=y_+MIN_WIDTH);
	}

}
