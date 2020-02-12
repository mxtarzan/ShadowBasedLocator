package prog04;
import java.util.ArrayList;

//
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/*--------------------------------------------------------------------------+
|	This version puts together all the things we have seen so far:			|
|		• GraphicObject class hierarchy										|
|		• an interface type that specifies functionality (here animation)	|
|		• Use of PImage as graphic content for objects.						|
|		• An example of keyframed animation;								|
|		• Use of back buffer rendering for double buffering and also		|
|			to produce PImage content for rendering.						|
|																			|
|	Keyboard commands:														|
|		• ' ' pauses/resumes animation										|
|		• 'b' set the animation mode to "BOX_WORLD"							|
|		• 'c' set the animation mode to "CYLINDER_WORLD"					|
|		• 'f' displays the objects' reference frames						|
|		• 'a' displays the objects' absolute bounding boxes					|
|		• 'r' displays the objects' relative bounding boxes					|
|		• 'n' disables display of the objects' bounding boxes				|
|																			|
|	Points worthy of notice:												|
|		1.  I use two enumerated types to store "mode" information: one	to	|
|			store the type of bounding box to use (BoxRenderingMode has		|
|			three possible values: absolute, relative, and none), and one 	|
|			to choose the animation mode (AnimationMode; currently only two	|
|			modes are implemented: BOX and CYLINDER).						|
|		2.	Support for bounding boxes has been moved to the GraphicObject	|
|			class.  Every GraphicObject subclass must implement the method	|
|			updateAbsoluteBoxes_().											|
|		3.	As discussed in class, I implemented an abstract parent class	|
|			KeyframeInterpolator, of which LinearKeyframeInterpolator,		|
|			CubicSplineKeyframeInterpolator, etc. would be subclasses that	|
|			implement a particular kind of interpolation.					|
|		4.	A KeyframeInterpolator is created with a list of key-frames to	|
|			work with, and will return the interpolated state vector for a	|
|			given time t.  This class should be enhanced by adding the		|
|			possibility for an animation to repeat a given number of times	|
|			or forever, to chain animations, etc.							|
|		5. 	Right now, only KeyframedFace uses a KeyframeInterpolator, but	|
|			really, any animated GraphicObject should be able to take a		|
|			KeyframeInterpolator as argument of its constructor (next 		|
|			revised version).												|
|																			|
|	Jean-Yves Hervé, Nov. 2012 (version for design grad students).			|
|					 Revised Nov. 2019 for CSC406.							| 
+--------------------------------------------------------------------------*/

public class EarShooterMainClass extends PApplet implements ApplicationConstants 
{
	//-----------------------------
	//	graphical objects
	//-----------------------------
	ArrayList<GraphicObject> objectList_;
	ArrayList<GraphicObject> deletedobj;

	//-----------------------------
	//	Various status variables
	//-----------------------------
	/**	Desired rendering frame rate
	 * 
	 */
	static final float RENDERING_FRAME_RATE = 60;
	
	/**	Ratio of animation frames over rendering frames 
	 * 
	 */
	static final int ANIMATION_RENDERING_FRAME_RATIO = 5;
	
	/**	computed animation frame rate
	 * 
	 */
	static final float ANIMATION_FRAME_RATE = RENDERING_FRAME_RATE * ANIMATION_RENDERING_FRAME_RATIO;
	
	
	/**	Variable keeping track of the last time the update method was invoked.
	 * 	The different between current time and last time is sent to the update
	 * 	method of each object. 
	 */
	private int lastUpdateTime_;
	
	/**	A counter for animation frames
	 * 
	 */
	private int frameCount_;


	/**	Whether to draw the reference frame of each object (for debugging
	 * purposes.
	 */
	private boolean drawRefFrame_ = false;

	/**	Used to pause the animation
	 */
	boolean animate_ = true;
	
	/**
	 * 	How we handle edges of the window
	 */
	private AnimationMode animationMode_ = AnimationMode.CYLINDER_WORLD;
	
	/**	The off-screen buffer used for double buffering (and display content
	 * for some rectangles.
	 */
	private PGraphics offScreenBuffer_;

	/**	Previous value of the off-screen buffer (after the last call to draw()
	 */
	private PGraphics lastBuffer_;

	/** Toggles on-off double buffering.
	 */
	private boolean doDoubleBuffer_ = false;

	private float time_ = 0;
	
	private int level = 1;
	
	public void settings() 
	{
		//  dimension of window in pixels
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void setup() 
	{    
		if (BAIL_OUT_IF_ASPECT_RATIOS_DONT_MATCH)
		{
			if (Math.abs(WORLD_HEIGHT - PIXEL_TO_WORLD*WINDOW_HEIGHT) > 1.0E5)
			{
				System.out.println("World and Window aspect ratios don't match");
				System.exit(1);
			}
		}
		GraphicObject.setAnimationMode(AnimationMode.SPHERE_WORLD);
		animationMode_ = AnimationMode.SPHERE_WORLD;
		frameRate(ANIMATION_FRAME_RATE);
		frameCount_ = 0;

		//	Loading data files should be done in the setup method
		PImage []image = {	loadImage("astroidpicture.png")};

		objectList_ = new ArrayList<GraphicObject>();
		deletedobj = new ArrayList<GraphicObject>();
		
		objectList_.add(new Ship());
		objectList_.add(new AnimatedBox(image[0], PIXEL_TO_WORLD/15, 1));
		
		//	I allocate my off-screen buffers at the same dimensions as the window
		offScreenBuffer_ = createGraphics(width, height);
		lastBuffer_ = createGraphics(width, height);
		//	Fill the last buffer with something (all white).
		lastBuffer_.beginDraw();
		lastBuffer_.fill(255);
		lastBuffer_.rect(0, 0, width, height);
		lastBuffer_.endDraw();
	}

	/** Processing sketch rendering callback function
	 * 
	 */
	public void draw()
	{
		//================================================================
		//	Only render one frame out of ANIMATION_RENDERING_FRAME_RATIO
		//================================================================
		if (frameCount_ % ANIMATION_RENDERING_FRAME_RATIO == 0)
		{
			PGraphics gc;
			if (doDoubleBuffer_) 
			{
				//	I say that the drawing will take place inside of my off-screen buffer
				gc = offScreenBuffer_;
				offScreenBuffer_.beginDraw();
			}
			else
				//	Otherwise, the "graphic context" is that of this PApplet
				gc = this.g;

			background(loadImage("background.jpg"));
			gc.fill(0xFFFFFFFF);
			gc.text("SCORE = " + str(((Ship)objectList_.get(0)).score_), 25, 25);
			gc.text("LIFE = " + str(((Ship)objectList_.get(0)).lives_), 25, 50);
			gc.text("HEALTH = " + str(((Ship)objectList_.get(0)).health_), 25, 75);
			gc.fill(((Ship)objectList_.get(0)).color_);
			// define world reference frame:  
			//    Origin at windows's center and 
			//    y pointing up
			//    scaled in world units
			gc.translate(WORLD_X, WORLD_Y); 
			gc.scale(DRAW_IN_WORLD_UNITS_SCALE, -DRAW_IN_WORLD_UNITS_SCALE);

			if (drawRefFrame_)
				GraphicObject.drawReferenceFrame(gc);

			if ((animationMode_ == AnimationMode.BOX_WORLD) || (animationMode_ == AnimationMode.WINDOW_WORLD))
			{
				for (GraphicObject obj : objectList_)
					obj.draw(gc);
			}
			else 
			{
				for (GraphicObject obj : objectList_)
					obj.drawAllQuadrants(gc);
			}
			
			if (doDoubleBuffer_)
			{
				offScreenBuffer_.endDraw();

				image(offScreenBuffer_, 0, 0);				
	
				//	For some reason this doesn't work and I don't understand why.
				lastBuffer_.beginDraw();
				lastBuffer_.image(offScreenBuffer_, 0, 0);
				lastBuffer_.endDraw();

				int []pixelLB = lastBuffer_.pixels;
				int []pixelOSB = offScreenBuffer_.pixels;
				//int nbPixels = width*height;
				//	Copy pixel info last buffer.  Since we flipped the y axis,
				//	we need to invert the row order while copying
				for (int i=0, k=height-1; i<height; i++,k--)
					for (int j=0; j<width; j++)
						pixelLB[k*width + j] = pixelOSB[i*width + j];
				
				lastBuffer_.updatePixels();
			}
		}

		//  and then update their state
		if (animate_)
		{
			update();
		}
		
		frameCount_++;
	}
	
	public void update() {

		int time = millis();

		if (animate_)
		{
			//  update the state of the objects ---> physics
			float dt = (time - lastUpdateTime_)*0.001f;
			
			for (GraphicObject obj : objectList_)
			{
				if (obj instanceof AnimatedObject)
					obj.update(dt);

			}
		}
		//ship hit detection
		Ship ship = (Ship) objectList_.get(0);
		for (int j = 1; j < objectList_.size(); j++) {
			if (objectList_.get(j) instanceof AnimatedBox && objectList_.get(j).isInside(ship.x_, ship.y_)) {
				//astroid hitting ship;
				if(((AnimatedBox)objectList_.get(j)).size_ == 1){
					deletedobj.add(objectList_.get(j));
					ship.lives_ -= 1;
				}
				else {
					deletedobj.add(objectList_.get(j));
					ship.health_ -= 25;
				}
				System.out.println("Ship life = " + ship.lives_ + " Ship health = " + ship.health_);
				if(ship.health_ <= 0) {
					ship.lives_ -=1;
					ship.health_ = 100;
				}
				if(ship.lives_ == 0){
					animate_ = false;
				}
			}
		}
		//missle hit detection
		for (int i = 1; i < objectList_.size(); i++) {
			if (objectList_.get(i) instanceof Missle) {
				Missle rocket = (Missle) objectList_.get(i);
				for (int j = 1; j < objectList_.size(); j++) {
					boolean modified = false;
					if (objectList_.get(j) instanceof Box && objectList_.get(j).isInside(rocket.x_, rocket.y_)) {
						//big astroid full damage;
						if(!modified && ((AnimatedBox)objectList_.get(j)).damage_ == 2 && ((AnimatedBox)objectList_.get(j)).size_ == 1)  {
							float initx = ((AnimatedBox)objectList_.get(j)).x_;
							float inity = ((AnimatedBox)objectList_.get(j)).y_;
							System.out.println(((AnimatedBox)objectList_.get(j)).damage_);
							deletedobj.add(objectList_.get(i));
							deletedobj.add(objectList_.get(j));
							objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15*2/3, 2, initx, inity));
							objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15*2/3, 2, initx, inity));
							objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15*2/3, 2, initx, inity));
							((Ship)objectList_.get(0)).score_ += 20;
							break;
							
						}
						//big astroid not damage;
						if(!modified && ((AnimatedBox)objectList_.get(j)).damage_ < 2 && ((AnimatedBox)objectList_.get(j)).size_ == 1) {
							((AnimatedBox)objectList_.get(j)).damage_ += 1;
							System.out.println(((AnimatedBox)objectList_.get(j)).damage_);
							deletedobj.add(objectList_.get(i));
							((Ship)objectList_.get(0)).score_ += 5;
							break;
						}
						//med astroid full damage;
						if(!modified && ((AnimatedBox)objectList_.get(j)).damage_ == 1 && ((AnimatedBox)objectList_.get(j)).size_ == 2) {
							float initx = ((AnimatedBox)objectList_.get(j)).x_;
							float inity = ((AnimatedBox)objectList_.get(j)).y_;
							System.out.println(((AnimatedBox)objectList_.get(j)).damage_);
							deletedobj.add(objectList_.get(i));
							deletedobj.add(objectList_.get(j));
							objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15*2/3*2/3, 3, initx, inity));
							objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15*2/3*2/3, 3, initx, inity));
							((Ship)objectList_.get(0)).score_ += 15;
							break;
						}
						//med astroid not damage;
						if(!modified && ((AnimatedBox)objectList_.get(j)).damage_ < 1 && ((AnimatedBox)objectList_.get(j)).size_ == 2) {
							((AnimatedBox)objectList_.get(j)).damage_ = 1;
							System.out.println(((AnimatedBox)objectList_.get(j)).damage_);
							deletedobj.add(objectList_.get(i));
							((Ship)objectList_.get(0)).score_ += 10;
							break;
						}
						//small astroid full damage;
						if(!modified &&((AnimatedBox)objectList_.get(j)).size_ == 3) {
							deletedobj.add(objectList_.get(i));
							deletedobj.add(objectList_.get(j));
							((Ship)objectList_.get(0)).score_ += 5;
							break;
						}
						break;      
					}
				}
			}
		}
		//remove old missles
		for (int i = 1; i < objectList_.size(); i++) {
			if (objectList_.get(i) instanceof Missle && objectList_.get(i).timealive_>3) {
				deletedobj.add(objectList_.get(i));
			}
		}
		//end of level detection
		int count = 0;
		for (GraphicObject obj : objectList_)
		{
			count++;
			if (!(obj instanceof AnimatedBox))
				count--;
		}
		if(count == 0) {
			level++;
			switch(level) {
				case 2:
					for(int i = 1; i < objectList_.size(); ++i) {
						deletedobj.add(objectList_.get(i));
					}
					objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15, 1));
					objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15, 1));
					break;
				case 3:
					for(int i = 1; i < objectList_.size(); ++i) {
						deletedobj.add(objectList_.get(i));
					}
					objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15, 1));
					objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15, 1));
					objectList_.add(new AnimatedBox(loadImage("astroidpicture.png"), PIXEL_TO_WORLD/15, 1));
					break;
				case 4:
					animate_ = false;
			}
		}
		objectList_.removeAll(deletedobj);
		deletedobj.clear();
		lastUpdateTime_ = time;
	}
	

	/** Processing callback function for keyboard events
	 * 
	 */
	public void keyReleased() 
	{
		switch(key) {
			case ' ':
				float currtime = millis();
				if(currtime - time_ < 333) break;
				time_ = currtime;
				Ship obj = (Ship) objectList_.get(0);
				float x = (float) ((-Ship.TRIANGLE_HEIGHT*3/5)*Math.sin(obj.angle_)+ obj.x_);
				float y = (float) ((Ship.TRIANGLE_HEIGHT*3/5)*Math.cos(obj.angle_) + obj.y_);
				float vx = (float) (-25*Math.sin(obj.angle_)+obj.vx_);
				float vy = (float) (25*Math.cos(obj.angle_)+obj.vy_);
				objectList_.add(new Missle(x, y, vx, vy));
				//objectList_.add(new Ship());
				break;
				
			case 'n':
				GraphicObject.setBoundingBoxMode(BoundingBoxMode.NO_BOX);
				break;
	
			case 'r':
				GraphicObject.setBoundingBoxMode(BoundingBoxMode.RELATIVE_BOX);
				break;
	
			case 'e':
				GraphicObject.setBoundingBoxMode(BoundingBoxMode.ABSOLUTE_BOX);
				break;
	
			case 'f':
				drawRefFrame_ = !drawRefFrame_;
				GraphicObject.setDrafReferenceFrame(drawRefFrame_);
				break;
	
			case 'b':
				GraphicObject.setAnimationMode(AnimationMode.BOX_WORLD);
				animationMode_ = AnimationMode.BOX_WORLD;
				break;
	
			case 'c':
				GraphicObject.setAnimationMode(AnimationMode.CYLINDER_WORLD);
				animationMode_ = AnimationMode.CYLINDER_WORLD;
				break;
				
			case 'v':
				GraphicObject.setAnimationMode(AnimationMode.SPHERE_WORLD);
				animationMode_ = AnimationMode.SPHERE_WORLD;
				break;
	
				
			case 't':
				GraphicObject.setAnimationMode(AnimationMode.TORUS_WORLD);
						animationMode_ = AnimationMode.TORUS_WORLD;
				break;
	
			case 'w':
				if(((Ship)objectList_.get(0)).health_ <=50) break;
				objectList_.get(0).vy_ += 2*Math.cos(objectList_.get(0).angle_);
				objectList_.get(0).vx_ -= 2*Math.sin(objectList_.get(0).angle_);
				break;
//			case 's':
//				objectList_.get(0).vy_ -= 2*Math.cos(objectList_.get(0).angle_);
//				objectList_.get(0).vx_ += 2*Math.sin(objectList_.get(0).angle_);
//				break;	
			case 'a':
				objectList_.get(0).spin_ += Math.PI/4;
				break;
				
			case 'd':
				objectList_.get(0).spin_ -= Math.PI/4;
				break;
				
//			case 'd':
//				doDoubleBuffer_ = !doDoubleBuffer_;
//				break;
//				
//			default:
//				break;
		}
	}

	public static void main(String[] argv)
	{
		PApplet.main("prog04.EarShooterMainClass");
	}

}
