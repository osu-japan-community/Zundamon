package community.japan.osu.Object;

import com.twitter.clientlib.model.User;
import community.japan.osu.SlashCommand;
import community.japan.osu.User.*;
import community.japan.osu.User.BanWord;
import community.japan.osu.User.Boshu;
import community.japan.osu.Yomiage.Yomiage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bot {

    JDA jda;
    String token;
    String vc_webhook;
    String multi_webhook;
    String api_key;
    String cloud_api_key;
    String db_user;
    String db_pass;

    public Bot() {
        Dotenv env = Dotenv.configure().load();
        token = env.get("BOT_TOKEN");
        vc_webhook = env.get("BOT_VC_WEBHOOK");
        multi_webhook = env.get("BOT_MULTI_WEBHOOK");
        api_key = env.get("BOT_API_KEY");
        cloud_api_key = env.get("CLOUD_API_KEY");
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
    public String getCloudApiKey() {
        return cloud_api_key;
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
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_PRESENCES
                ).enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ACTIVITY,
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
                        new Outh(),
                        new Join(),
                        new RoleDistribution(),
                        new Boshu(),
                        new BanWord(),
                        new ManageUser()
                )
                .build();

        jda.updateCommands().queue();

        jda.upsertCommand("connect", "vcに接続します").queue();
        jda.upsertCommand("disconnect", "vcから切断します").queue();
    }
}