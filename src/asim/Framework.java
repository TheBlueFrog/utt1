package asim;

import asim.agents.Environment;

import java.util.ArrayList;
import java.util.List;


public class Framework
{
	private final Environment mEnvironment;

	public Framework() {
		this.mEnvironment = new Environment(this);
	}

//	public void broadcast(Message message)
//	{
//		for (Entity a : entities)
//			a.incoming(message);
//	}

}
