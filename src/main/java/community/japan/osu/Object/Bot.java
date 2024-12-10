package community.japan.osu.Object;

import community.japan.osu.SlashCommand;
import community.japan.osu.User.Outh;
import community.japan.osu.Yomiage.Yomiage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Bot {

    JDA jda;
    String token;
    String vc_webhook;
    String multi_webhook;
    String api_key;
    String db_user;
    String db_pass;

    public Bot() {
        Dotenv env = Dotenv.configure().load();
        token = env.get("BOT_TOKEN");
        vc_webhook = env.get("BOT_VC_WEBHOOK");
        multi_webhook = env.get("BOT_MULTI_WEBHOOK");
        api_key = env.get("BOT_API_KEY");
        db_user = env.get("DB_USER");
        db_pass = env.get("DB_PASS");
    }

    public JDA getJda() {
        return jda;
    }

    public String getDbUser() {
        return db_user;
    }

    public String getDbPass() {
        return db_pass;
    }

    public String getApiKey() {
        return api_key;
    }

    public String getMultiWebhook() {
        return multi_webhook;
    }

    public String getVCWebhook() {
        return vc_webhook;
    }

    public String getToken() {
        return token;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost/OJC?autoReconnect=true",
                    db_user,
                    db_pass
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadJDA() {
        jda = JDABuilder.createDefault(this.token)
                .setRawEventsEnabled(true)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS
                ).enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.EMOJI
                )
                .disableCache(
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS
                ).setActivity(
                        Activity.playing("Osu! Japan Community"))
                .addEventListeners(
                        new Yomiage(),
                        new SlashCommand(),
                        new Outh()
                )
                .build();

        jda.updateCommands().queue();

        jda.upsertCommand("connect", "vcに接続します").queue();
        jda.upsertCommand("disconnect", "vcから切断します").queue();
        jda.upsertCommand("tuwabo", "通話相手を募集します")
                .addOption(OptionType.CHANNEL, "チャンネル", "参加してほしいチャンネルは指定してください", true).
                addOption(OptionType.STRING, "コメント", "一緒に送信するコメントを入力してください", true).queue();
        jda.upsertCommand("multibo", "マルチプレイの相手を募集します")
                .addOption(OptionType.STRING, "ルーム名", "マルチプレイの名前を入力してください", true)
                .addOption(OptionType.STRING, "パスワード", "無ければ「なし」と書いてください", true)
                .addOption(OptionType.STRING, "難易度", "どのユーザー向けにルームを作成しますか？", true, true)
                .addOption(OptionType.STRING, "vcの有無", "vcへの参加は必須ですか？", true, true)
                .addOption(OptionType.STRING, "コメント", "コメントを入力してください", true)
                .queue();
    }
}