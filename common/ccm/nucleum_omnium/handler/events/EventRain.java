package ccm.nucleum_omnium.handler.events;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.ForgeSubscribe;
import ccm.nucleum_omnium.utils.lib.Properties;

public class EventRain
{

    @ForgeSubscribe
    public void rainStopper(final PlaySoundEvent event)
    {
        if (Properties.rain){
            if ((event != null) && (event.source != null) && (event.source.soundName.startsWith("ambient/weather/rain"))){
                event.result = null;
            }
        }
    }
}