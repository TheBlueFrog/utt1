package asim;

public abstract class ScoringAgent extends Agent
{
	public ScoringAgent(Framework f)
	{
		super(f);
	}

	/**
	 * a new StateNode has been added by the device, the ScoringAgent
	 * should evaluate the situation and return a score.
	 * 
	 * @param node
	 * @return	this agents score, value of 1.0 is a neutral value and
	 * nets out to nothing.  Negative values are down-vote positive values
	 * are up-votes.  
	 * 
	 * This score contributes to the system's real-time score by
	 * multiplying the old score by this score.  See ScoreKeeper class
	 */
	abstract public double score (StateNode node);
	
	@Override
	protected void consume(Message msg) 
	{
		StateNode start = (StateNode) msg.mMessage;
		double d = score (start);

		send(ScoreKeeper.class, d);
	}

}
