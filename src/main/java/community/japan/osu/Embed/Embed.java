package community.japan.osu.Embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public abstract class Embed {

    public static EmbedBuilder getVCDisconnect() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**切断しました**", "VCから切断したのだ！", false);
        eb.setImage("https://cdn.discordapp.com/attachments/944984741826932767/1242040479135170641/image.png");
        eb.setColor(Color.RED);

        return eb;
    }
}
