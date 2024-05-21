package community.japan.osu.Ticket.Event;

import community.japan.osu.Embed.Embed;
import community.japan.osu.Main;
import community.japan.osu.Object.Bot;
import community.japan.osu.Object.Ticket;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.EnumSet;

public class CreateTicket extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getChannel().getIdLong() == Main.ticket.getSUPPORT_CHANNEL_ID()) {

            if(e.getMember().getUser().isBot()) {
                return;
            }

            JDA jda = e.getJDA();

            jda.getGuildById(e.getGuild().getIdLong()).getTextChannelById(Main.ticket.getSUPPORT_CHANNEL_ID()).sendMessageEmbeds(Embed.getTicketWizard().build())
                    .addActionRow(
                            Button.primary("btn_create_ticket", "チケットを作成")
                    ).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        if(e.getButton().getId().equals("btn_create_ticket")) {

            Guild g = e.getGuild();
            Ticket ticket = Main.ticket;

            // 初期値: 1

            int ticketNo = 1;

            // アクティブなチケットと閉じているチケットの最後の数字を取得する
            for (GuildChannel ch : g.getChannels()) {
                if (ch.getName().contains("ticket-")) {
                    ticketNo = Math.max(ticketNo, Integer.parseInt(ch.getName().replace("ticket-", "")) + 1);
                } else if (ch.getName().contains("closed-")) {
                    ticketNo = Math.max(ticketNo, Integer.parseInt(ch.getName().replace("closed-", "")) + 1);
                }
            }

            //チケットを作成する
            g.createTextChannel("ticket-" + String.format("%04d", ticketNo), g.getCategoryById(ticket.getSUPPORT_CATEGORY_ID()))
                    .addPermissionOverride(e.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    // 管理人: 1089160067263246347
                    // (変える)
                    .addRolePermissionOverride(1093865053448589342L, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    // (変える)
                    .addRolePermissionOverride(1194245046321545327L, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(g.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();

            e.reply("Created your ticket!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {

        if(e.getChannel().getName().contains("ticket-")) {

            Bot bot = Main.bot;
            JDA jda = bot.getJda();

            jda.getGuildById(e.getGuild().getIdLong()).getTextChannelById(e.getChannel().getIdLong()).sendMessageEmbeds(Embed.getIntializeTicket().build()).
                    addActionRow(
                            Button.danger("btn_close", "Close")
                    ).queue();
        }
    }
}
