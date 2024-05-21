package community.japan.osu.Object;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private long GUILD_ID;
    private long SUPPORT_CATEGORY_ID;
    private long SUPPORT_CHANNEL_ID;
    private List<Long> ticket_owners;

    public Ticket() {

        Dotenv dotenv = Dotenv.configure()
                .load();

        GUILD_ID = Long.parseLong(dotenv.get("GUILD_ID"));
        SUPPORT_CATEGORY_ID = Long.parseLong(dotenv.get("SUPPORT_CATEGORY"));
        SUPPORT_CHANNEL_ID = Long.parseLong(dotenv.get("SUPPORT_CHANNEL"));
        ticket_owners = new ArrayList<>();
    }

    public long getGUILD_ID() {
        return GUILD_ID;
    }

    public long getSUPPORT_CATEGORY_ID() {
        return SUPPORT_CATEGORY_ID;
    }

    public long getSUPPORT_CHANNEL_ID() {
        return SUPPORT_CHANNEL_ID;
    }

    public List<Long> getTicket_owners() {
        return ticket_owners;
    }

    public void setTicket_owners(List<Long> ticket_owners) {
        this.ticket_owners = ticket_owners;
    }
}
