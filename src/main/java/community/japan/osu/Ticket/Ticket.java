package community.japan.osu.Ticket;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ticket extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        System.out.println("a");
    }
}
