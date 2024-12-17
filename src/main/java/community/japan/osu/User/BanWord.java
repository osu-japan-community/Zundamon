package community.japan.osu.User;

import community.japan.osu.Main;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BanWord extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.isFromGuild()) {
            return;
        }

        if (e.getGuild().getIdLong() != 1089160066797686846L) {
            return;
        }

        if (e.getAuthor().isBot()) {
            return;
        }

        Connection connection = Main.bot.getConnection();
        PreparedStatement ps;

        // 追加
        if(e.getChannel().getIdLong() == 1318436292220158023L) {
            try {
                if(!Main.banWords.contains(e.getMessage().getContentRaw().toLowerCase())) {

                    Main.banWords.add(e.getMessage().getContentRaw().toLowerCase());

                    ps = connection.prepareStatement("insert into banword (content) values (?)");
                    ps.setString(1, e.getMessage().getContentRaw().toLowerCase());
                    ps.executeUpdate();

                    e.getMessage().addReaction(
                            Emoji.fromUnicode("U+2705")
                    ).queue();
                } else {
                    e.getMessage().addReaction(
                            Emoji.fromUnicode("U+274C")
                    ).queue();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // 削除
        else if (e.getChannel().getIdLong() == 1318436346045534208L) {
            try {

                Main.banWords.remove(e.getMessage().getContentRaw().toLowerCase());

                ps = connection.prepareStatement("delete from banword where content = ?");
                ps.setString(1, e.getMessage().getContentRaw().toLowerCase());
                ps.executeUpdate();

                e.getMessage().addReaction(
                        Emoji.fromUnicode("U+2705")
                ).queue();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // Wordを削除
        else {
            if(Main.banWords.isEmpty()) {
                return;
            }

            for (String bannedWord : Main.banWords) {
                if (e.getMessage().getContentRaw().toLowerCase().contains(bannedWord)) {
                    e.getMessage().delete().queue();
                    break;
                }
            }
        }

        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
