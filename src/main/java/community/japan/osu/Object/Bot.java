package community.japan.osu.Object;

import community.japan.osu.SlashCommand;
import community.japan.osu.Yomiage.Yomiage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {

    JDA jda;
    String token;

    public Bot() {
        Dotenv env = Dotenv.configure().load();
        token = env.get("BOT_TOKEN");
    }

    public JDA getJda() {
        return jda;
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
                        new SlashCommand()
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