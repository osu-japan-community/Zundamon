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

    public static EmbedBuilder getOuthMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("<:check:1305395371169546280> **アカウント認証**",
                "ようこそ！Osu! Japan Communityへ\n" +
                        "あなたのOsu! Japan Community での生活はまだ始まっていません。\n" +
                        "すべてのチャンネルへアクセスするために認証してください。", false);
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getRoleDistributionMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**<:check:1305395371169546280> ロール付与**");
        eb.setDescription("* リアクションをクリックするとロールを付与するのだ！");
        eb.addField("<:modeosu2x:1091054411276615700> Standard", "<@&1089162880278069258>", true);
        eb.addField("<:modetaiko2x:1091054413096964157> Taiko", "<@&1089163590193389698>", true);
        eb.addField("<:modefruits2x:1091054408437071923> Catch the Beat", "<@&1089162928462233661>", true);
        eb.addField("<:modemania2x:1091054415781302275> Mania", "<@&1089162961572077688>", true);
        eb.addField(":flag_white: Mania 4K", "<@&1316380765071736834>", true);
        eb.addField(":flag_black: Mania 7K", "<@&1316380834076561440>", true);
        eb.addField(":loud_sound: 通話募集", "<@&1089160067187757093>", true);
        eb.addField(":video_game: マルチ募集", "<@&1089914703326748772>", true);
        eb.addField(":paintbrush: Skinner", "<@&1090228065461882901>", true);
        eb.addField(":map: Mapper", "<@&1090228160714526762>", true);
        eb.addField(":notes: 作曲家", "<@&1090514847667597373>", true);
        eb.addField(":art: 絵師", "<@&1090879466185703454>", true);

        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getBoshuMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("<:people:1316704452060643348> **通話/マルチ募集**");
        eb.setDescription("通話やマルチの相手を募集します");
        eb.addField("<:book:1316753856557744299> **使い方**",
                "以下のボタンをクリックして早速始めましょう!", false);
        eb.setColor(Color.BLACK);

        return eb;
    }
}
