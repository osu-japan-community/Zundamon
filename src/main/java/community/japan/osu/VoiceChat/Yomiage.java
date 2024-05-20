package community.japan.osu.VoiceChat;

import community.japan.osu.Embed.Embed;
import community.japan.osu.Main;
import community.japan.osu.VoiceChat.Audio.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
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

        if (e.getVoiceState().getChannel() == null) {
            return;
        }

        if (e.getVoiceState().getChannel().getIdLong() != Main.vc.getVC_CHANNEL()) {
            return;
        }

        for (Member m : e.getVoiceState().getChannel().getMembers()) {
            //ボットか他の読み上げbot、自分がいたら参加しないように
            if (m.getUser().getIdLong() == 727508841368911943L || m.getUser().getIdLong() == 1242032355221180457L) {
                isVC = true;
            }
        }

        if(!isVC) {
            AudioManager audioManager = e.getGuild().getAudioManager();
            audioManager.openAudioConnection(e.getVoiceState().getChannel());
        }

        Main.vc.setVC(true);
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

        if (e.getMessage().getContentRaw().equals("!disconnect")) {

            e.getGuild().getAudioManager().closeAudioConnection();
            e.getMessage().replyEmbeds(Embed.getVCDisconnect().build()).queue();

            Main.vc.setVC(false);
            
        } else {

            try {
                String message;

                if (e.getMessage().getContentRaw().contains("http") || e.getMessage().getContentRaw().contains("http")) {
                    message = "url";
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

            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


}

