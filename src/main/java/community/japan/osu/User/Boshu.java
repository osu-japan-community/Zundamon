package community.japan.osu.User;

import community.japan.osu.Embed;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public class Boshu extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if(e.getGuild().getIdLong() != 1089160066797686846L) return;
        if(e.getChannel().getIdLong() != 1316755517980541009L) return;

        if(e.getMessage().getContentRaw().equals("create")) {

            e.getChannel().sendMessageEmbeds(Embed.getBoshuMessage().build())
                    .addComponents(
                            ActionRow.of (Button.primary("tuwabo", "通話募集"),
                            Button.success("multibo", "マルチ募集"))
                    ).queue();
        }
    }

    private StringSelectMenu.Builder createSelectMenu(StringSelectMenu.Builder builder) {

        builder.addOption("Xへの自動投稿をオン", "on");
        builder.addOption("Xへの自動投稿をオフ", "off");

        return builder;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        if(e.getComponentId().equals("tuwabo")) {
            StringSelectMenu.Builder builder = StringSelectMenu.create("menu:dropdown:tuwabo");

            e.replyComponents(ActionRow.of(
                    createSelectMenu(builder).build()
            )).setEphemeral(true).queue();

        } else if (e.getComponentId().equals("multibo")) {
            StringSelectMenu.Builder builder = StringSelectMenu.create("menu:dropdown:multibo");

            e.replyComponents(ActionRow.of(
                    createSelectMenu(builder).build()
            )).setEphemeral(true).queue();
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent e) {
        if (e.getComponentId().equals("menu:dropdown:multibo")) {
            for(String s : e.getValues()) {
                if(s.equals("on")) {

                } else {

                }
            }
        } else if (e.getComponentId().equals("menu:dropdown:tuwabo")) {
            for(String s : e.getValues()) {
                if(s.equals("on")) {

                } else {

                }
            }
        }
    }
}
