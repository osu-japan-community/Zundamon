package community.japan.osu.Yomiage;

import com.moji4j.MojiConverter;
import community.japan.osu.Embed;
import community.japan.osu.Main;
import community.japan.osu.Yomiage.Audio.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yomiage extends ListenerAdapter {

    static final String urlPattern = "https?://\\S+";

    // ずんだもん本人なら「さん」をつけない
    private String getPersonName(Member member) {

        String UserName = member.getEffectiveName();

        if(member.getNickname() != null) {
            UserName = member.getNickname();
        }

        if(UserName.length() >= 5) {
            UserName = UserName.substring(0, 4);
        }

        if (!member.getUser().getId().equals(Main.jda.getSelfUser().getId())) {
            UserName += "さん、";
        }

        return UserName;
    }

    public static int countEnglishLetters(String text) {
        int count = 0;
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch) && ch >= 'A' && ch <= 'z') {
                count++;
            }
        }
        return count;
    }

    private static String extractURL(String text) {
        // URLの正規表現パターン
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();  // 最初に見つかったURLを返す
        }
        return null;  // URLが見つからなければnullを返す
    }


    // 読み上げ時をするときに適切な形に
    private String editUserMessageForServer(String message) throws IOException {

        MojiConverter converter = new MojiConverter();

        if(message.contains("https://") || message.contains("http://")) {

            if(extractURL(message) != null) {

                try {
                    Document doc = Jsoup.connect(extractURL(message)).get();

                    message = message.replaceAll(urlPattern, "") + "。";
                    message = message.replaceAll("[\\p{Punct}\\p{S}]", "");
                    message += converter.convertRomajiToHiragana(doc.title()) + "のurl。";
                } catch (HttpStatusException e) {
                    message = "リンク省略なのだ";
                }
            }
        } else {
            // 英語が10文字以上だと読み上げないように
            if(countEnglishLetters(message) > 10) {
                System.out.println(message);
                return null;
            }
            message = converter.convertRomajiToHiragana(message);
        }

        if (message.length() > 256) {
            message = message.substring(0, 256) + "、長すぎるので省略するのだ";
        }

        return message;
    }

    // メッセージに送信されたテキストをずんだもんに読み上げてもらう
    private Path getConvertWavPath(Member m, String message) throws URISyntaxException, IOException, InterruptedException {

        JSONObject json;
        int voiceID = Main.voiceChat.getId() + 1;

        HttpClient client = HttpClient.newHttpClient();

        System.out.println(getPersonName(m) + editUserMessageForServer(message));

        HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:50021/audio_query?speaker=1&text=" + URLEncoder.encode(getPersonName(m) + editUserMessageForServer(message), StandardCharsets.UTF_8)))
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        json = new JSONObject(response.body());
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:50021/synthesis?speaker=" + Main.voiceChat.getSpeaker()))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<byte[]> r = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // 変換成功
        if (r.statusCode() == 200) {
            Files.write(Path.of( (voiceID) +".wav"), r.body());
        } else {
            System.out.println("Error: " + response.statusCode());
        }

        Main.voiceChat.setId(voiceID);

        return Path.of("%s.wav".formatted(String.valueOf(voiceID)));

    }

    // 自動参加
    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent e) {

        long[] yomiageBotID = new long[] {727508841368911943L, 533698325203910668L, 472911936951156740L};

        //botがVCに参加しているかを取得
        boolean inVoiceChannel = Main.voiceChat.isInVoiceChannel();
        JDA jda = e.getJDA();

        // 1090163808556818552 (聞き専1)

        if(e.getVoiceState().getChannel() == null) {
            return;
        }

        if(e.getVoiceState().getChannel().getIdLong() != Main.voiceChat.getVC_CHANNEL()) {
            return;
        }


        // 聞き専 or 自分
        for (Member m : e.getVoiceState().getChannel().getMembers()) {
            for(long y : yomiageBotID) {
                if(m.getIdLong() == y || m.getIdLong() == e.getJDA().getSelfUser().getIdLong()) {
                    inVoiceChannel = true;
                    break;
                }
            }
        }

        // VCへの参加と「参加しました」メッセージの送信
        if (!inVoiceChannel) {
            AudioManager audioManager = e.getGuild().getAudioManager();
            audioManager.openAudioConnection(e.getVoiceState().getChannel());
            jda.getGuildById(e.getGuild().getIdLong()).getTextChannelById(Main.voiceChat.getVC_TEXT()).sendMessageEmbeds(
                    Embed.getJoinVoiceChatMessage().build()
            ).queue();

            inVoiceChannel = true;
        }

        Main.voiceChat.setInVoiceChannel(inVoiceChannel);
    }

    // 読み上げ部分
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        boolean inVoiceChannel = Main.voiceChat.isInVoiceChannel();

        if (!inVoiceChannel) {
            return;
        }

        if(e.getAuthor().isBot()) {
            return;
        }

        if (e.getChannel().getIdLong() != Main.voiceChat.getVC_TEXT()) {
            return;
        }

        if(e.getMember().getUser().isBot()) {
            return;
        }

        if(e.getMember().getVoiceState() == null) {
            return;
        }

        try {

            String message = e.getMessage().getContentRaw().replaceAll("<@\\d+>", "");

            if (e.getMessage().getContentRaw().isEmpty()) {
                message = "なんかのファイルが添付されたのだ";
            }

            if(!message.contains("https://") && !message.contains("http://")) {
                message = message.replaceAll("<:\\w+:\\d+>", "");
                message = message.replaceAll("<\\w+:\\w+:\\d+>", "");
                message = message.replaceAll("[ -/:-@\\[-`{-~]", "");;
            }

            Main.voiceChat.setId(Main.voiceChat.getId() + 1);
            PlayerManager.getINSTANCE().loadAndPlay(e.getGuild(), getConvertWavPath(e.getMember(), message).toString());
        } catch (URISyntaxException | InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent e) {

        int id = Main.voiceChat.getId() + 1;
        String message;

        try {
            if (e.getChannelLeft() == null || e.getChannelJoined() != null) {

                if (e.getVoiceState().getChannel().getIdLong() != Main.voiceChat.getVC_CHANNEL()) {
                    return;
                }

                message = getPersonName(e.getMember()) + "がVCに参加したのだ";
                PlayerManager.getINSTANCE().loadAndPlay(e.getGuild(), getConvertWavPath(e.getMember(), message).toString());
                Main.voiceChat.setId(id);
            }

            if (e.getChannelLeft() != null || e.getChannelJoined() == null) {
                    // 自身がどのVCにも参加していない
                    if (e.getGuild().getSelfMember().getVoiceState() == null || e.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
                        return;
                    }
                    // 退出されたチャンネルが自身のいるVCと異なる
                    if (e.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() != e.getChannelLeft().getIdLong()) {
                        return;
                    }

                    // VCに残ったユーザーが全員Bot、または誰もいなくなった
                    boolean existsUser = e
                            .getChannelLeft()
                            .getMembers()
                            .stream()
                            .anyMatch(member -> !member.getUser().isBot()); // Bot以外がいるかどうか

                    if (existsUser) {
                        message = getPersonName(e.getMember()) + "がVCから退出したのだ";
                        PlayerManager.getINSTANCE().loadAndPlay(e.getGuild(), getConvertWavPath(e.getMember(), message).toString());
                        Main.voiceChat.setId(id);
                        return;
                    }

                    e.getGuild().getAudioManager().closeAudioConnection();
                    Main.voiceChat.setInVoiceChannel(false);
                }

        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException();
        }

    }
}
