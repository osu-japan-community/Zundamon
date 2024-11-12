package community.japan.osu;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Date;

public abstract class Embed {

    public static EmbedBuilder getJoinVoiceChatMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**<:check:1305395371169546280> 接続完了**", "* VCに接続したのだ！\n" +
                "* https://discord.com/channels/1089160066797686846/1089160068689309713 に書かれたメッセージを読み上げるのだ！", false);
        eb.setTimestamp(new Date().toInstant());
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getDisconnectVoiceChatMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**<:x_:1305398003904942120> 切断完了**",
                "* VCから切断したのだ！\n" +
                        "* ``/connect``かミュートを行うとまたVCに参加するのだ！", false);
        eb.setTimestamp(new Date().toInstant());
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getErrorMessage(String error) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**<:warning:1305402733200932894> エラーが発生しました**", "* " + error, false);
        eb.setTimestamp(new Date().toInstant());
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getInviteSuccessMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("<:check:1305395371169546280> **送信完了**", "* https://discord.com/channels/1089160066797686846/1089160068689309714 に招待メッセージを送信したのだ！", false);
        eb.setTimestamp(new Date().toInstant());
        eb.setColor(Color.BLACK);

        return eb;
    }
}
