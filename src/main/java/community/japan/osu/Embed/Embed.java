package community.japan.osu.Embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public abstract class Embed {

    public static EmbedBuilder getVCDisconnect() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**切断しました**", "VCから切断したのだ！", false);
        eb.setColor(Color.RED);

        return eb;
    }
}
