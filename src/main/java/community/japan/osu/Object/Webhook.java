package community.japan.osu.Object;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.WebhookCluster;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import community.japan.osu.Main;
import okhttp3.OkHttpClient;

public class Webhook {

    long ch_id;
    String webhook;
    String token;

    public Webhook(long ch_id, String webhook) {
        this.ch_id = ch_id;
        this.webhook = webhook;
        this.token = Main.bot.getToken();
    }

    private WebhookClient buildWebhookClient() {

        WebhookClientBuilder builder = new WebhookClientBuilder(this.webhook);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("ずんだもん");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);

        WebhookClient client = builder.build();
        WebhookCluster cluster = new WebhookCluster(5);
        cluster.setDefaultHttpClient(new OkHttpClient());
        cluster.setDefaultDaemon(true);
        cluster.buildWebhook(ch_id, this.token);

        cluster.addWebhooks(client);

        return client;
    }

    public void sendWebhookMessage(String role, WebhookEmbed embed) {
        WebhookClient client = buildWebhookClient();
        WebhookMessageBuilder builder = new WebhookMessageBuilder();

        builder.setContent(role);
        builder.setUsername("ずんだもん").addEmbeds(embed);
        builder.setAvatarUrl(Main.bot.getJda().getSelfUser().getAvatarUrl());

        client.send(builder.build());
    }
}
