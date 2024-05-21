package community.japan.osu.Ticket.Event;

import community.japan.osu.Embed.Embed;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class CloseTicket extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        if(e.getButton().getId().equals("btn_close")) {
            e.replyEmbeds(Embed.getCloseTicket().build())
                    .addActionRow(
                            Button.primary("btn_yes", "終了"),
                            Button.danger("btn_no", "キャンセル")
                    ).queue();
        }
        else if (e.getButton().getId().equals("btn_yes")) {
            e.getChannel().asTextChannel().getManager().setName("closed-" + e.getChannel().getName().replace("ticket-", "")).queue();
            e.getChannel().asTextChannel().getPermissionOverrides().remove(e.getMember());

            e.getMessage().editMessageEmbeds(Embed.getClosedTicket().build()).setComponents().queue();

            e.reply("チケットを閉じました！").setEphemeral(true).queue();
        }
        else if (e.getButton().getId().equals("btn_no")) {
            e.getMessage().delete().queue();
            e.reply("処理をキャンセルしました！").setEphemeral(true).queue();
        }
    }
}
