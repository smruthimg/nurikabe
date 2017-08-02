package sample;

import java.util.Collection;

/**
 * Created by smrut on 5/20/2017.
 */
public interface Configuration {
    public Collection<Configuration>getSuccessors();
    public boolean isValid();
    public boolean isGoal();

}
