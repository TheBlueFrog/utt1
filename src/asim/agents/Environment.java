package asim.agents;

import asim.Entity;
import asim.Log;
import asim.Message;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/12/2015.
 */
public class Environment {

    private static final long ONE_SECOND = 1;

    static private List<Entity> mEntities = new ArrayList<Entity>();
    private final Simulator mSimulator;

    public String getTag()
    {
        return this.getClass().getSimpleName();
    }

    public Environment(Simulator simulator) {
        Log.d(getTag(), String.format("Construct Environment"));

        mSimulator = simulator;
        setupEnvironment();
    }

    static private long mCurrentTime = 0;

    public long getTime (){
        return mCurrentTime;
    }

    public long incTime()
    {
        return ++mCurrentTime;
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
        Entity e = new Entity(this);
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

    /**
     *
     * @param class1
     * @return
     */
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

    /**
     * run the environment to the given time
     *
     * simple, just look from now till then and go one
     * time unit
     *
     * this could get a lot smarter
     *
     * @param time
     */
    public long runTo(long time) {
        Log.d(getTag(), String.format("start runTo, now %d, stop at %d", getTime(), time));

        while (getTime() <= (time - 1)) {
            long t = getTime() + ONE_SECOND;
            for (Entity e : mEntities) {
                e.advance(t);
            }

            incTime();
        }

        Log.d(getTag(), String.format("end runTo, now %d", getTime()));
        return getTime();
    }
}

