package lib.cofh.api.core;

/**
 * Dummy class used to initialize Cape and Skin Registers to prevent accidental NPEs.
 * 
 * @author Zeldo Kavira
 */
public class NullSimpleRegistry implements ISimpleRegistry {
    
    @Override
    public boolean register(final String playerName, final String URL) {
        
        return false;
    }
    
}