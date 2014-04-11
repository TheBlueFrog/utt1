package asim;

import java.util.ArrayList;
import java.util.List;


public class Framework
{
	private List<Agent> agents = new ArrayList<Agent>();

	public void send(Message m)
	{
		if (m.mRecipient == null)
			throw new IllegalStateException("Message has no recient");
		
		for (Agent a : getRecipients(m.mRecipient))
			a.incoming(m);
	}

//	public void broadcast(Message message)
//	{
//		for (Agent a : agents)
//			a.incoming(message);
//	}

	public void register(Agent agent)
	{
		agents.add(agent);
	}

	private List<Agent> getRecipients(Class<? extends Agent> class1)
	{
		List<Agent> v = new ArrayList<Agent>();
		
		for (Agent a : agents)
		{
			if (class1.isAssignableFrom(a.getClass()))
				v.add(a);
		}
		
		return v;
	}
}
