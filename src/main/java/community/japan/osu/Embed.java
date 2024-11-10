package community.japan.osu;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Date;

public abstract class Embed {

    public static EmbedBuilder getJoinVoiceChatMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**接続完了**", "VCに接続したのだ！", false);
        eb.setTimestamp(new Date().toInstant());
        eb.setColor(Color.BLACK);

        return eb;
    }
}
