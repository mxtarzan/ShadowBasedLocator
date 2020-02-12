package prog04;

public class LinearKeyframeInterpolator extends KeyframeInterpolator {

	public LinearKeyframeInterpolator(float[][] keyframes_) {
		super(keyframes_);
		// TODO Auto-generated constructor stub
	}

	public float[] computeStateVector(float t)
	{
		final int T_INDEX = keyframes_[0].length-1;
		final int DIM_STATE_VECT = keyframes_[0].length-1;
		final int NUM_FRAMES = keyframes_.length;
		
		double t0 = keyframes_[0][T_INDEX];
		double tf = keyframes_[NUM_FRAMES-1][T_INDEX];

		float []stateVect = new float[DIM_STATE_VECT];

		//	Before t0, the vehicle is at the first keyframe
		if (t <= t0)
		{
			for (int k=0; k<DIM_STATE_VECT; k++)
				stateVect[k] = keyframes_[0][k];
		}			
		//	We are between two keyframes_  t0 < t ≤ t_n-1
		else if (t <=  tf)
		{
			int i = getIntervalIndex(t);
			
			//	Now we need to interpolate between frames i and i+1
			//------------------------------------------------------
			//	First, compute the fraction of the time interval already traveled
			float tau= getTau(t, i);
			
			for (int k=0; k<DIM_STATE_VECT; k++)
			{
				stateVect[k] = keyframes_[i][k] + tau*(keyframes_[i+1][k]-keyframes_[i][k]);
			}
		}
		//	After tf, the vehicle is at the last keyframe
		else
		{
			for (int k=0; k<DIM_STATE_VECT; k++)
				stateVect[k] = keyframes_[NUM_FRAMES-1][k];
		}		
		
		return stateVect;
	}

}
