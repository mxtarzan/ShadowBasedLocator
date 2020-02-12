package prog04;

/**	I use this interface to store all application-wide constants
 * 	relative to dimensions of the window and world.  Classes that
 * 	require access to this information just need to
 * 	"implement ApplicationConstants".
 * 
 * 	All calculations are made for a world reference frame with axes
 * 	parallel to that of the window:
 * 		- x axis horizontal and pointing to the right of the window
 * 		- y axis vertical and pointing up in the window. 
 * 	 
 * 	This is the version we did in class, somewhat cleaned up,
 * 	with javadoc comments for documentation, and some regular
 * 	comments to attract your attention to features and weaknesses.
 * 
 * 	In this version of the interface, we set the dimensions (width and height)
 * 	of the window (in pixels) and the bounds of the world (XMIN, XMAX, YMIN, YMAX
 *  in world units).  All the scales, conversion factors, and location of the 
 *  world's origin relative to the window (in pixels) are calculated from these 6 values.
 * 
 * @author Jean-Yves Hervé
 *
 */
public interface ApplicationConstants {

	/**	Set this constant to true if you want to ensure that you don't 
	 * 	end up with different aspect ratios for world and window (which 
	 * 	could could result in either clipping at top
	 * 	and bottom of the window (if WORLD_HEIGHT < PIXEL_TO_WORLD*WINDOW_WIDTH)
	 *	or non-painted bands at the top and bottom of the window
	 *	(if WORLD_HEIGHT < PIXEL_TO_WORLD*WINDOW_WIDTH) 
	 */
	boolean BAIL_OUT_IF_ASPECT_RATIOS_DONT_MATCH = true;
	
	
	//-----------------------------------------------------------
	//	Dimensions in pixels
	//-----------------------------------------------------------
	
	//	I define the dimensions of my display window.  These
	//	values will be used as-is in the app's settings() method.
	
	/**	Window width (in pixels)
	 */
	int	WINDOW_WIDTH = 1600;

	/**	Window height (in pixels)
	 */
	int WINDOW_HEIGHT = 1600;

	//-----------------------------------------------------------
	//	Dimensions in world units
	//-----------------------------------------------------------

	//	Next I compute the range of x and y values for my world.
	//	In class, I hard-coded this because we had set the origin at the center
	//	of the world, but these values would break down if we moved that origin.
	//	It is preferable to calculate the proper values.

	/** Lower bound for x coordinate (in world units), seen at left edge of the window.
	 * 	It is simply the horizontal pixel travel from the world's origin to the left
	 * 	edge of the window, converted to world units.  This value could be positive
	 *	if the world's origin is outside the window, to its left
	 */
	float XMIN = -50f;
	
	/** Upper bound for x coordinate (in world units), seen at right edge of the window.
	 * 	It is simply the horizontal pixel travel from the world's origin to the right
	 * 	edge of the window, converted to world units.  This value could be positive
	 *	if the world's origin is outside the window, to its left
	 */
	float XMAX = 50f;
	
	/** Lower bound for y coordinate (in world units), seen at bottom edge of the window.
	 * 	It is simply the vertical pixel travel from the world's origin to the bottom
	 * 	edge of the window, converted to world units, and sign inverted so that y
	 * 	points"up".  This value could be positive
	 *	if the world's origin is outside the window, above it
	 */
	float YMIN = -50f;
	
	/** Upper bound for y coordinate (in world units), seen at top edge of the window.
	 * 	It is simply the vertical pixel travel from the world's origin to the top
	 * 	edge of the window, converted to world units, and sign inverted so that y
	 * 	points"up.  This value could be negative
	 *	if the world's origin is outside the window, below it
	 */
	float YMAX = 50f;

	
	//============================================================
	//============================================================
	//	Everything below this point is automatically calculated
	//	(don't modify).
	//============================================================
	//============================================================
	
	//-----------------------------------------------------------
	//	Dimensions in world units
	//-----------------------------------------------------------

	//	Let's say that my app shows insects moving around on a 4cm x 3cm plate.  
	//	Here I make sure that my world dimensions have the same aspect ratio as
	//	the windows.  I could check for this in the setup() method and bail out
	//	if it is not the case
	
	/**	Width of my world (in world units, here: cm)
	 */
	float WORLD_WIDTH = XMAX - XMIN;	
	
	/**	Height of my world (in world units, here: cm)
	 */
	float WORLD_HEIGHT = YMAX - YMIN;
	
	//	Now I compute my conversion scales, using only the horizontal
	//	dimensions.  Again, if the aspect ratios are not the same, this
	//	will result in vertical cropping, or horizontal bands at the top and
	//	bottom of the window not used for drawing.

	//	After some thought, I have realized that the names lead to confusion,
	//	so I have introduced two new constants
	
	/**	Conversion factor from world units to pixels (for calculations).  
	 */
	float WORLD_TO_PIXEL = WINDOW_WIDTH/WORLD_WIDTH;

	/**	Conversion factor from pixels to world units (for calculations).  
	 */
	float PIXEL_TO_WORLD = 1.f/WORLD_TO_PIXEL;
	
	/**	Scale that needs to be applied in order to draw stuff in 
	 * 	world units.  
	 */
	float DRAW_IN_WORLD_UNITS_SCALE = WORLD_TO_PIXEL;

	/**	Scale that needs to be applied in order to draw stuff in 
	 * 	pixels (e.g. a strokeWeight) while rendering an object set in
	 * 	world units.  
	 */
	float DRAW_IN_PIXELS_SCALE = PIXEL_TO_WORLD;
	

	//-----------------------------------------------------------
	//	Dimensions in pixels
	//-----------------------------------------------------------

	//	Next I say where the origin of my world should located
	//	relative to my window.  In theory, this location could 
	//	be outside of the window.
	//	Here I put the world's origin at the center of the window.
	
	/**	Windows horizontal coordinate (in pixels) of the world's
	 * 	origin:  displacement in pixels from the left edge of the window.
	 * 	Positive iff XMIN < 0
	 */
	float WORLD_X = (0 - XMIN)*WORLD_TO_PIXEL;

	/**	Windows vertical coordinate (in pixels) of the world's
	 * 	origin:  displacement in pixels from the top edge of the window.
	 *  Positive iff YMAX > 0
	 */
	float WORLD_Y = (YMAX - 0)*WORLD_TO_PIXEL;
	
	
	
	int	NORTH = 0, NORTH_WEST = 1, WEST =2, SOUTH_WEST = 3, SOUTH = 4,
			SOUTH_EAST = 5, EAST = 6, NORTH_EAST = 7;
	
}
