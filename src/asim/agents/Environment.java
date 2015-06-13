package asim.agents;

import asim.Agent;
import asim.Framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by mike on 6/12/2015.
 */
public class Environment {

    static private Environment mEnvironment;
    static private List<Agent> mAgents;

    public long getTime (){
        return System.currentTimeMillis();
    }

    public Environment (Framework f) {
        mFramework = f;
        mEnvironment = f.setupEnvironment();
        setupEntities();
    }
    private static void setupEntities()
    {
        for (Class<? extends Agent> x : mAgents)
        {
            Constructor<? extends Agent> c = x.getConstructor(Framework.class);
            Agent a = c.newInstance(mFramework);
            a.start();
        }
    }


    private static Environment setupEnvironment() {
        // init the environment the entities will be in
        Environment e = new Environment();
        return e;
    }
    public Agent getAgent (int index)
    {
        return mAgents.get(index);
    }
}

