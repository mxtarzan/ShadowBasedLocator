package prog04;

public abstract class KeyframeInterpolator 
{
	protected float [][]keyframes_;
	
	public KeyframeInterpolator(float [][]keyframes) 
	{
		//	consider making a copy
		keyframes_ = keyframes;
	}
	
	public abstract float[] computeStateVector(float t);
	
	protected int getIntervalIndex(float t)
	{
		final int T_INDEX = keyframes_[0].length-1;

		//	First, find the index i such that t_i < t <= t_i+1
		int i=0;
		while (t > keyframes_[i+1][T_INDEX])
			i++;

		return i;
	}
	
	protected float getTau(float t, int i)
	{
		final int T_INDEX = keyframes_[0].length-1;

		//	Now we need to interpolate between frames i and i+1
		//------------------------------------------------------
		//	First, compute the fraction of the time interval already traveled
		return (t - keyframes_[i][T_INDEX]) / (keyframes_[i+1][T_INDEX] - keyframes_[i][T_INDEX]);
	}
}
