package community.japan.osu;

import community.japan.osu.Object.Bot;
import community.japan.osu.Object.VoiceChat;
import net.dv8tion.jda.api.JDA;

public class Main {


    public static Bot bot;
    public static JDA jda;
    public static VoiceChat voiceChat;

    // 起動
    public static void main(String[] args) {

        bot = new Bot();
        voiceChat = new VoiceChat();

        bot.loadJDA();
        jda = bot.getJda();
    }
}