package community.japan.osu.Object;

import io.github.cdimascio.dotenv.Dotenv;

public class Ticket {

    private long GUILD_ID;
    private long SUPPORT_CATEGORY_ID;
    private long SUPPORT_CHANNEL_ID;

    public Ticket() {

        Dotenv dotenv = Dotenv.configure()
                .load();

        GUILD_ID = Long.parseLong(dotenv.get("GUILD_ID"));
        SUPPORT_CATEGORY_ID = Long.parseLong(dotenv.get("SUPPORT_CATEGORY"));
        SUPPORT_CHANNEL_ID = Long.parseLong(dotenv.get("SUPPORT_CHANNEL"));
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
}
