package community.japan.osu.VoiceChat;

import community.japan.osu.Embed.Embed;
import community.japan.osu.Main;
import community.japan.osu.VoiceChat.Audio.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.json.JSONObject;

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

public class Yomiage extends ListenerAdapter {

    private Path getConvertWavPath(Member member, String message) throws URISyntaxException, IOException, InterruptedException {

        JSONObject queryJson;
        String address = "";

        if (!member.getUser().getName().equals("ずんだもん")) {
            address = "さん、";
        }

        if (message.length() > 200) {
            message = message.substring(0, 200) + "、以下省略";
        }

        String name = getUserName(member) + address;

        int id = Main.vc.getId() + 1;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:50021/audio_query?speaker=1&text=" + URLEncoder.encode(name + message, StandardCharsets.UTF_8)))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        queryJson = new JSONObject(response.body());

        httpClient = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:50021/synthesis?speaker=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(queryJson.toString()))
                .build();
        HttpResponse<byte[]> r = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (r.statusCode() == 200) {
            Files.write(Path.of( (id) +".wav"), r.body());
        } else {
            System.out.println("Error: " + response.statusCode());
        }

        Main.vc.setId(id);

        return Path.of("%s.wav".formatted(String.valueOf(id)));
    }

    private String getUserName(Member member) {
        return member.getNickname() == null ? member.getEffectiveName() : member.getNickname();
    }

    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent e) {

        boolean isVC = Main.vc.getVC();
        JDA jda = Main.bot.getJda();

        if (e.getVoiceState().getChannel() == null) {
            return;
        }

        if (e.getVoiceState().getChannel().getIdLong() != Main.vc.getVC_CHANNEL()) {
            return;
        }

        for (Member m : e.getVoiceState().getChannel().getMembers()) {
            //ボットか他の読み上げbot、自分がいたら参加しないように
            if (m.getUser().getIdLong() == 1240649156167471186L) {
                isVC = true;
            }
        }

        if(!isVC) {
            AudioManager audioManager = e.getGuild().getAudioManager();
            audioManager.openAudioConnection(e.getVoiceState().getChannel());
            jda.getGuildById(e.getGuild().getIdLong()).getTextChannelById(Main.vc.getVC_TEXT()).sendMessageEmbeds(Embed.getVCConnect().build()).queue();
        }

        Main.vc.setVC(true);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if(e.getName().equals("disconnect")) {

            boolean isVC = Main.vc.getVC();

            if(!isVC || e.getMember().getVoiceState().getChannel() == null) {
                e.replyEmbeds(Embed.getError("VCに接続していないのだ！").build()).setEphemeral(true).queue();
                return;
            }

            if(e.getMember().getVoiceState().getChannel().getIdLong() != Main.vc.getVC_CHANNEL()) {
                e.replyEmbeds(Embed.getError("VCに接続していないのだ！").build()).setEphemeral(true).queue();
                return;
            }

            if(e.getChannel().getIdLong() != Main.vc.getVC_TEXT()) {
                e.replyEmbeds(Embed.getError("このチャンネルではこのコマンドは使えないのだ！").build()).setEphemeral(true).queue();
                return;
            }

            e.getGuild().getAudioManager().closeAudioConnection();

            e.replyEmbeds(Embed.getVCDisconnect().build()).queue();

            Main.vc.setVC(false);
        }
    }

    // !disconnect
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        boolean isVC = Main.vc.getVC();

        if(!isVC) {
            return;
        }

        if(e.getChannel().getIdLong() != Main.vc.getVC_TEXT()) {
            return;
        }

        if(e.getMember().getUser().isBot()) {
            return;
        }

        if(e.getMember().getVoiceState() == null) {
            return;
        }

            try {
                String message;

                if (e.getMessage().getContentRaw().contains("http") || e.getMessage().getContentRaw().contains("http")) {
                    message = "なんかのurlなのだ";
                } else if (e.getMessage().getContentRaw().isEmpty()) {
                    message = "なんかのファイル添付なのだ";
                } else {
                    message = e.getMessage().getContentRaw().replaceAll("<@\\d+>", "");
                    message = message.replaceAll("<:\\w+:\\d+>", "");
                    message = message.replaceAll("<\\w+:\\w+:\\d+>", "");
                    message = message.replaceAll("[ -/:-@\\[-`{-~]", "");;
                }

                Main.vc.setId(Main.vc.getId() + 1);
                PlayerManager.getINSTANCE().loadAndPlay(e.getGuild(),  getConvertWavPath(e.getMember(), message).toString());

            } catch (URISyntaxException | InterruptedException | IOException ex) {
                throw new RuntimeException(ex);
            }
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent e) {

        JDA jda = Main.bot.getJda();

        int id = Main.vc.getId() + 1;

        try {
            // 誰かがVCに参加したとき
            if (e.getChannelLeft() == null || e.getChannelJoined() != null) {

                if (e.getVoiceState().getChannel().getIdLong() != Main.vc.getVC_CHANNEL()) {
                    return;
                }

                PlayerManager.getINSTANCE().loadAndPlay(e.getGuild(), getConvertWavPath(e.getMember(), "がVCに参加しました").toString());
                Main.vc.setId(id);

                return;
            }

            // 誰かがVCから退出したとき
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
                        PlayerManager.getINSTANCE().loadAndPlay(e.getGuild(), getConvertWavPath(e.getMember(), "がVCから退出しました").toString());
                        Main.vc.setId(id);
                        return;
                    }

                    e.getGuild().getAudioManager().closeAudioConnection();
                    jda.getGuildById(e.getGuild().getIdLong()).getTextChannelById(Main.vc.getVC_TEXT()).sendMessageEmbeds(Embed.getVCAutoDisconnect().build()).queue();
                    Main.vc.setVC(false);
                }
        } catch (URISyntaxException | IOException | InterruptedException e1) {
            throw new RuntimeException(e1);
        }
    }
}

