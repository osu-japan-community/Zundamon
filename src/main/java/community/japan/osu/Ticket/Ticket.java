package community.japan.osu.Ticket;

import community.japan.osu.Embed.Embed;
import community.japan.osu.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ticket extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if(e.getName().equals("set-ticket")) {

            if(e.getChannel().getIdLong() != Main.ticket.getSUPPORT_CHANNEL_ID()) {
                e.replyEmbeds(Embed.getError("このチャンネルではこのコマンドは使えないのだ！").build()).setEphemeral(true).queue();
                return;
            }

            e.reply("wip feature").setEphemeral(true).queue();
        }
    }
}
