package community.japan.osu.Ticket.Event;

import community.japan.osu.Embed.Embed;
import community.japan.osu.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

import static community.japan.osu.Main.ticket;

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

            e.getMessage().editMessageEmbeds(Embed.getClosedTicket().build())
                    .setComponents(
                            ActionRow.of(
                                    Button.danger("btn_delete", "削除")
                            )
                    )
                    .queue();

            e.reply("チケットを閉じました！").setEphemeral(true).queue();
        }
        else if (e.getButton().getId().equals("btn_no")) {
            e.getMessage().delete().queue();
            e.replyEmbeds(Embed.getCancelCloseTicket().build()).setEphemeral(true).queue();
        }
        else if (e.getButton().getId().equals("btn_delete")) {
            e.getChannel().delete().queue();

            List<Long> ticket = Main.ticket.getTicket_owners();

            ticket.remove(e.getMember().getIdLong());
            Main.ticket.setTicket_owners(ticket);
        }
    }
}
