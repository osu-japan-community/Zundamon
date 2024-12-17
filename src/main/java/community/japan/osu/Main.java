package community.japan.osu;

import community.japan.osu.Object.Bot;
import community.japan.osu.Object.VoiceChat;
import net.dv8tion.jda.api.JDA;

import java.util.List;

public class Main {


    public static Bot bot;
    public static JDA jda;
    public static VoiceChat voiceChat;
    public static List<String> banWords;

    // 起動
    public static void main(String[] args) {

        bot = new Bot();
        voiceChat = new VoiceChat();

        bot.loadBanWord();

        bot.loadJDA();
        jda = bot.getJda();
        banWords = bot.getBanWords();
    }
}