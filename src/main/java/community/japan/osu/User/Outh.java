package community.japan.osu.User;

import community.japan.osu.Embed;
import community.japan.osu.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Outh extends ListenerAdapter {

    private static TextInput createTextInput(String id, String label, String description, boolean isRequire, TextInputStyle style) {

        return TextInput.create(id, label, style)
                .setMinLength(1)
                .setPlaceholder(description)
                .setRequired(isRequire)
                .build();
    }



    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;

        if(e.getGuild().getIdLong() != 1089160066797686846L) return;

        if (e.getChannel().getIdLong() != 1316056673177178213L) return;

        e.getChannel().sendMessageEmbeds(
                Embed.getOuthMessage().build()
        ).addActionRow(
                Button.success("btn_outh", "認証する")
        ).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {
        if(e.getModalId().equals("form_outh")) {

            try {
                Connection connection = Main.bot.getConnection();
                PreparedStatement ps;
                ResultSet result;
                Role role = e.getGuild().getRoleById(1091204734842572891L);

                String name = e.getValue("name").getAsString();
                name = name.replace(" ", "_");
                String url = "https://osu.ppy.sh/api/get_user?k=" + Main.bot.getApiKey() + "&u=" + name;

                HttpClient httpClient = HttpClient.newHttpClient();

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                JSONArray jsonResponse = new JSONArray(response.body());
                if (!jsonResponse.isEmpty()) {
                    JSONObject user = jsonResponse.getJSONObject(0);
                    String userId = user.getString("user_id");

                    try {

                        ps = connection.prepareStatement("select id from users where id = ?");
                        ps.setLong(1, e.getUser().getIdLong());
                        result = ps.executeQuery();
                        if (result.next()) {
                            e.getGuild().addRoleToMember(e.getMember(), role).queue();

                            e.reply("認証成功").setEphemeral(true).queue();

                            e.getGuild().getTextChannelById(1316076149239054336L).sendMessage(
                                    "認証成功: " + e.getUser().getName() + " -> " + user.getString("username") + " (" + user.getString("country") + ")"
                            ).queue();

                            return;
                        }

                        ps = connection.prepareStatement("select id from users where bancho = ?");
                        ps.setInt(1, Integer.parseInt(userId));
                        result = ps.executeQuery();

                        if(!result.next()) {
                            ps = connection.prepareStatement("insert into users (id, bancho) values (?, ?)");
                            ps.setLong(1, e.getUser().getIdLong());
                            ps.setInt(2, Integer.parseInt(userId));
                            ps.executeUpdate();
                            e.getGuild().addRoleToMember(e.getMember(), role).queue();

                            e.reply("認証成功").setEphemeral(true).queue();

                            e.getGuild().getTextChannelById(1316076149239054336L).sendMessage(
                                    "認証成功: " + e.getUser().getName() + " -> " + user.getString("username") + " (" + user.getString("country") + ")"
                            ).queue();
                        } else {
                            e.replyEmbeds(Embed.getErrorMessage(
                                    "このアカウントは既に他のアカウントで認証されています。\n" +
                                            "* 認証できるBanchoアカウントは一つまでです。"
                            ).build()).setEphemeral(true).queue();

                        }
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.replyEmbeds(Embed.getErrorMessage(
                            "ユーザーが見つかりませんでした。有効なユーザー名を入力して下さい。"
                    ).build()).setEphemeral(true).queue();
                }

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        if (e.getButton().getId().equals("btn_outh")) {
            TextInput input = createTextInput("name", "osu!のユーザー名", "例: peppy", true, TextInputStyle.SHORT);
            Modal modal = Modal.create("form_outh", "アカウントを認証する")
                    .addActionRows(
                            ActionRow.of(input)
                    ).build();
            e.replyModal(modal).queue();
        }
    }
}
