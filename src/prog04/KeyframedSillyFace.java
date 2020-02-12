package prog04;

import prog04.KeyframeInterpolator;

public class KeyframedSillyFace extends SillyFace implements AnimatedObject {

	public static final	int X_INDEX = 0, Y_INDEX = 1, θ_INDEX = 2, RED_INDEX = 3, GREEN_INDEX = 4, T_INDEX = 5;
	public static final	int NUM_INDICES = 5;
	
	private float time_;
	private KeyframeInterpolator interpolator_;

	public KeyframedSillyFace(KeyframeInterpolator theInterpolator) 
	{
		super();
		
		interpolator_ = theInterpolator;
		time_ = 0;
		float []state = interpolator_.computeStateVector(0);
		x_ = state[X_INDEX];
		y_ = state[Y_INDEX];
		angle_ = state[θ_INDEX];
		color_ = 0xFF000000 | 
				 (int) state[RED_INDEX] << 16| 
				 ((int) state[GREEN_INDEX] << 8) | 
				 (0 /*(int) state[BLUE_INDEX]*/);
		
	}
	
	public void update(AnimationMode animationMode, float dt)
	{
		float t = time_ + dt;
		float []state = interpolator_.computeStateVector(t);
		x_ = state[X_INDEX];
		y_ = state[Y_INDEX];
		angle_ = state[θ_INDEX];
		color_ = 0xFF000000 | 
				 (int) state[RED_INDEX] << 16| 
				 ((int) state[GREEN_INDEX] << 8) | 
				 (0 /*(int) state[BLUE_INDEX]*/);
		updateAbsoluteBoxes_();
		time_ = t;
	}
}
