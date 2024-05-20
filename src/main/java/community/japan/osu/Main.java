package community.japan.osu;

import community.japan.osu.Object.Bot;
import community.japan.osu.Object.Ticket;
import community.japan.osu.Object.VC;

public class Main {

    public static Bot bot;
    public static Ticket ticket;
    public static VC vc;

    public static void main(String[] args) {

        bot = new Bot();
        ticket = new Ticket();
        vc = new VC();

        bot.loadJDA();
    }
}