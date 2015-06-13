package asim.agents;

/**
 * Created by mike on 6/12/2015.
 */
public class Simulator {
    private Environment mEnvironment;

    public Simulator() {
        mEnvironment = new Environment(this);
    }
}
