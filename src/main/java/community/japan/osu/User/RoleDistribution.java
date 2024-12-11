package community.japan.osu.User;

import community.japan.osu.Embed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

public class RoleDistribution extends ListenerAdapter {
    final long MAPPER = 1090228160714526762L;
    final long SKINNER = 1090228065461882901L;
    final long SAKKYOKU = 1090514847667597373L;
    final long ESI = 1090879466185703454L;
    final long STD = 1089162880278069258L;
    final long CTB = 1089162928462233661L;
    final long MANIA = 1089162961572077688L;
    final long TAIKO = 1089163590193389698L;
    final long MULTI = 1089914703326748772L;
    final long TUUWA = 1089160067187757093L;
    final long FOURK = 1316380765071736834L;
    final long SEVENK = 1316380834076561440L;

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getGuild().getIdLong() != 1089160066797686846L) return;

        if(e.getChannel().getIdLong() != 1091053198925627432L) return;

        if (!e.getAuthor().isBot()) {
            e.getChannel().sendMessageEmbeds(Embed.getRoleDistributionMessage().build()).queue();
        } else {
            e.getMessage().addReaction(Emoji.fromFormatted("<:modeosu2x:1091054411276615700>")).queue();
            e.getMessage().addReaction(Emoji.fromFormatted("<:modetaiko2x:1091054413096964157>")).queue();
            e.getMessage().addReaction(Emoji.fromFormatted("<:modefruits2x:1091054408437071923>")).queue();
            e.getMessage().addReaction(Emoji.fromFormatted("<:modemania2x:1091054415781302275>")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F3F3")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F3F4")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F50A")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F3AE")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F58C")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F5FA")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F3B6")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F3A8")).queue();
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.getGuild().getIdLong() != 1089160066797686846L) return;

        if(e.getChannel().getIdLong() != 1091053198925627432L) return;

        Role mapper = e.getGuild().getRoleById(MAPPER);
        Role skinner = e.getGuild().getRoleById(SKINNER);
        Role sakkyoku = e.getGuild().getRoleById(SAKKYOKU);
        Role esi = e.getGuild().getRoleById(ESI);
        Role std = e.getGuild().getRoleById(STD);
        Role ctb = e.getGuild().getRoleById(CTB);
        Role mania = e.getGuild().getRoleById(MANIA);
        Role taiko = e.getGuild().getRoleById(TAIKO);
        Role multi = e.getGuild().getRoleById(MULTI);
        Role tuuwa = e.getGuild().getRoleById(TUUWA);
        Role fourk = e.getGuild().getRoleById(FOURK);
        Role sevenK = e.getGuild().getRoleById(SEVENK);

        if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modeosu2x:1091054411276615700>"))) {
            e.getGuild().addRoleToMember(e.getMember(), std).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modetaiko2x:1091054413096964157>"))) {
            e.getGuild().addRoleToMember(e.getMember(), taiko).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modefruits2x:1091054408437071923>"))) {
            e.getGuild().addRoleToMember(e.getMember(), ctb).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modemania2x:1091054415781302275>"))) {
            e.getGuild().addRoleToMember(e.getMember(), mania).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F50A"))) {
            e.getGuild().addRoleToMember(e.getMember(), tuuwa).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3AE"))) {
            e.getGuild().addRoleToMember(e.getMember(), multi).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F58C"))) {
            e.getGuild().addRoleToMember(e.getMember(), skinner).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F5FA"))) {
            e.getGuild().addRoleToMember(e.getMember(), mapper).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3B6"))) {
            e.getGuild().addRoleToMember(e.getMember(), sakkyoku).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3A8"))) {
            e.getGuild().addRoleToMember(e.getMember(), esi).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3F3"))) {
            e.getGuild().addRoleToMember(e.getMember(), fourk).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3F4"))) {
            e.getGuild().addRoleToMember(e.getMember(), sevenK).queue();
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        RestAction<Member> member = e.retrieveMember();

        if (e.getGuild().getIdLong() != 1089160066797686846L) return;

        if(e.getChannel().getIdLong() != 1091053198925627432L) return;

        Role mapper = e.getGuild().getRoleById(MAPPER);
        Role skinner = e.getGuild().getRoleById(SKINNER);
        Role sakkyoku = e.getGuild().getRoleById(SAKKYOKU);
        Role esi = e.getGuild().getRoleById(ESI);
        Role std = e.getGuild().getRoleById(STD);
        Role ctb = e.getGuild().getRoleById(CTB);
        Role mania = e.getGuild().getRoleById(MANIA);
        Role taiko = e.getGuild().getRoleById(TAIKO);
        Role multi = e.getGuild().getRoleById(MULTI);
        Role tuuwa = e.getGuild().getRoleById(TUUWA);
        Role fourk = e.getGuild().getRoleById(FOURK);
        Role sevenK = e.getGuild().getRoleById(SEVENK);

        if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modeosu2x:1091054411276615700>"))) {
            e.getGuild().removeRoleFromMember(member.complete(), std).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modetaiko2x:1091054413096964157>"))) {
            e.getGuild().removeRoleFromMember(member.complete(), taiko).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modefruits2x:1091054408437071923>"))) {
            e.getGuild().removeRoleFromMember(member.complete(), ctb).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted("<:modemania2x:1091054415781302275>"))) {
            e.getGuild().removeRoleFromMember(member.complete(), mania).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F50A"))) {
            e.getGuild().removeRoleFromMember(member.complete(), tuuwa).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3AE"))) {
            e.getGuild().removeRoleFromMember(member.complete(), multi).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F58C"))) {
            e.getGuild().removeRoleFromMember(member.complete(), skinner).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F5FA"))) {
            e.getGuild().removeRoleFromMember(member.complete(), mapper).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3B6"))) {
            e.getGuild().removeRoleFromMember(member.complete(), sakkyoku).queue();
        } else if(e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3A8"))) {
            e.getGuild().removeRoleFromMember(member.complete(), esi).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3F3"))) {
            e.getGuild().removeRoleFromMember(member.complete(), fourk).queue();
        } else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode("U+1F3F4"))) {
            e.getGuild().removeRoleFromMember(member.complete(), sevenK).queue();
        }
    }

}
