package community.japan.osu.Message;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Join extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin (GuildMemberJoinEvent e) {
        e.getJDA().getGuildById(1089160066797686846L).getTextChannelById(
                1089160068454424634L
        ).sendMessage(e.getMember().getAsMention() + " さん、よろしくお願いします :grin:").queue();
    }
}
