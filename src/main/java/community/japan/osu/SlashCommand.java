package community.japan.osu;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import community.japan.osu.Object.Bot;
import community.japan.osu.Object.Webhook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class SlashCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        // VCからずんだもんを強制的に切断する
        if (e.getName().equals("disconnect")) {
            boolean isinVoiceChat = Main.voiceChat.isInVoiceChannel();

            if (!isinVoiceChat || e.getJDA().getAudioManagers().isEmpty()) {
                e.replyEmbeds(
                        Embed.getErrorMessage("VCに接続していないのだ！").build()
                ).setEphemeral(true).queue();
                return;
            }

            e.getJDA().getAudioManagers().forEach(AudioManager::closeAudioConnection);

            e.replyEmbeds(
                    Embed.getDisconnectVoiceChatMessage().build()
            ).queue();

            Main.voiceChat.setInVoiceChannel(false);
        }
        else if (e.getName().equals("connect")) {

            long[] yomiageBotID = Main.voiceChat.getYOMIAGE_BOT_ID();
            //botがVCに参加しているかを取得
            boolean inVoiceChannel = Main.voiceChat.isInVoiceChannel();

            // 1090163808556818552 (聞き専1)

            try {
                if (e.getMember().getVoiceState().getChannel() == null) {
                    e.replyEmbeds(
                            Embed.getErrorMessage("VC1に接続していないのだ！").build()
                    ).setEphemeral(true).queue();
                    return;
                }
            } catch (NullPointerException ex) {
                e.replyEmbeds(
                        Embed.getErrorMessage("VC1に接続していないのだ！").build()
                ).setEphemeral(true).queue();
                return;
            }

            if(e.getMember().getVoiceState().getChannel().getIdLong() != Main.voiceChat.getVC_CHANNEL()) {
                e.replyEmbeds(
                        Embed.getErrorMessage("VC1に接続していないのだ！").build()
                ).setEphemeral(true).queue();
                return;
            }


            // 聞き専 or 自分
            for (Member m : e.getMember().getVoiceState().getChannel().getMembers()) {
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
                audioManager.openAudioConnection(e.getMember().getVoiceState().getChannel());
                e.replyEmbeds(
                        Embed.getJoinVoiceChatMessage().build()
                ).queue();
                inVoiceChannel = true;
            }

            Main.voiceChat.setInVoiceChannel(inVoiceChannel);
        }
    }
 }
