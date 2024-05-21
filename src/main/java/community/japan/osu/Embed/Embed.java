package community.japan.osu.Embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Date;

public abstract class Embed {

    public static EmbedBuilder getVCConnect() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**:white_check_mark: 接続しました**", "VCに接続したのだ！", false);
        eb.setColor(Color.GREEN);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getVCDisconnect() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**:x: 切断しました**", "VCから切断したのだ！", false);
        eb.setColor(Color.RED);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getVCAutoDisconnect() {

        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("**:x: 切断しました**", "誰もいなくなったのでVCから切断したのだ！", false);
        eb.setColor(Color.RED);
        eb.setTimestamp(new Date().toInstant());

        return eb;

    }

    public static EmbedBuilder getTicketWizard() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Ticket");
        eb.setTitle("チケットを作成すると運営からのサポートを受けることができます");
        eb.setColor(Color.CYAN);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getError(String error) {


        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**:warning: エラーが発生しました**", error, false);
        eb.setColor(Color.RED);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }
}
