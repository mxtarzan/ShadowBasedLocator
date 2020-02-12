package prog04;

/**	Enumerated type describing the different ways that objects that behave
 * relative to the edges of the window/screen
 * 
 * @author jyh
 *
 */
public enum AnimationMode 
{	/**	
	 *	Objects ignore window borders
	 */
	WINDOW_WORLD,
	
	/**
	 * Objects bounce on the edges of the window
	 */
	BOX_WORLD,
	
	/**
	 * Objects bounce on the top and bottom edges, wrap around the left
	 * and right edges
	 */
	CYLINDER_WORLD,
	
	/**
	 * Objects wrap around all edges
	 */
	SPHERE_WORLD,
	
	/**
	 * Objects wrap around all edges
	 */
	TORUS_WORLD
};
