package org.woahoverflow.chad.core.listener;

import org.woahoverflow.chad.handle.logging.LogLevel;
import org.woahoverflow.chad.core.ChadVar;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

import java.util.*;


public class OnReady
{
    @SuppressWarnings("unused")
    @EventSubscriber
    public void onReadyEvent(ReadyEvent e)
    {
        e.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "");

        //TODO: put this in its own thread class so i can change the timings on it
        ChadVar.EXECUTOR_POOL.submit(() -> {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!ChadVar.ROTATE_PRESENCE)
                        return;
                    Object[] ar = ChadVar.PRESENCE_ROTATION.toArray();
                    int rotation = ar.length;
                    e.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, (String)ar[new Random().nextInt(rotation)]);
                }
            }, 0, ChadVar.ROTATION_TIME); // this cant be changed for some reason, i would probably have to reschedule the timer in order for this to work
        });

        // UI Begin
        ChadVar.UI_HANDLER.addLog("Bot started with " + e.getClient().getGuilds().size() + " guilds!", LogLevel.INFO);
        ChadVar.UI_HANDLER.update();

        // Caching
        ChadVar.CACHE_DEVICE.cacheAll();
    }
}
