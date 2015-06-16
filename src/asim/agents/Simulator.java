package asim.agents;

/**
 * Created by mike on 6/12/2015.
 */
public class Simulator extends Thread {
    private Environment mEnvironment;

    public Simulator() {
        mEnvironment = new Environment(this);
    }

    @Override
    public void run()
    {
        mEnvironment.runTo(10);
    }
}
