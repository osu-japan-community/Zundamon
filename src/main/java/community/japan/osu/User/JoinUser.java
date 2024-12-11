package community.japan.osu.User;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinUser extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        e.getGuild().getTextChannelById(
                1089160068454424634L
        ).sendMessage(
                e.getMember().getAsMention() + "さん、よろしくお願いします :grin:"
        ).queue();
    }
}
