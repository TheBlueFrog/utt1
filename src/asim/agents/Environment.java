package asim.agents;

import asim.Entity;
import asim.Framework;
import asim.Message;
import asim.MutableStateNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/12/2015.
 */
public class Environment {

    static private Environment mEnvironment;
    static private List<Entity> mEntities;
    private final Simulator mSimulator;

    public Environment(Simulator simulator) {
        mSimulator = simulator;
        setupEnvironment();
    }

    public long getTime (){
        return System.currentTimeMillis();
    }

    private void setupEnvironment() {
        // init the environment the entities will be in
        try {
            setupEntities();
        } catch (NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e1) {
            e1.printStackTrace();
        }
    }
    private void setupEntities() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //for (Entity x : mEntities)
        Entity e = new Entity(mEnvironment);
        e.start();
    }

    public Entity getAgent (int index)
    {
        return mEntities.get(index);
    }

    public void register(Entity entity)
    {
        mEntities.add(entity);
    }

    public void send(Message m)
    {
        if (m.mRecipient == null)
            throw new IllegalStateException("Message has no recient");

        for (Entity a : getRecipients(m.mRecipient))
            a.incoming(m);
    }


    private List<Entity> getRecipients(Class<? extends Entity> class1)
    {
        List<Entity> v = new ArrayList<Entity>();

        for (Entity a : mEntities)
        {
            if (class1.isAssignableFrom(a.getClass()))
                v.add(a);
        }

        return v;
    }

}

