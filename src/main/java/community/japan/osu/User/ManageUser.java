package community.japan.osu.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import community.japan.osu.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageUser extends ListenerAdapter {

    private static JsonNode getJsonNode(String endpoint) throws IOException {
        URL obj = new URL(endpoint);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.toString());
    }

    private static boolean isSafeURL(String uri) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the `webRiskServiceClient.close()` method on the client to safely
        // clean up any remaining background resources.

        JsonNode jsonNode = getJsonNode("https://webrisk.googleapis.com/v1/uris:search" +
                "?threatTypes=MALWARE" +
                "&threatTypes=SOCIAL_ENGINEERING" +
                "&threatTypes=UNWANTED_SOFTWARE" +
                "&threatTypes=SOCIAL_ENGINEERING_EXTENDED_COVERAGE" +
                "&uri=" + uri + "&key=" + Main.bot.getCloudApiKey());

        return jsonNode.isEmpty();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String regex = "(https?://\\S+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(e.getMessage().getContentRaw());

        if (e.getJDA().getSelfUser() == e.getAuthor()) {
            return;
        }

        if(matcher.find()) {
            String url =  matcher.group(1);
            try {
                if(!isSafeURL(url)) {
                    e.getMessage().delete().queue();
                    System.out.println("脅威を検知したため、urlを削除しました。");
                }
            } catch (IOException ex) {
                //e.getMessage().delete().queue();
                throw new RuntimeException(ex);
            }
        }
    }
}
