package community.japan.osu.Object;

import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.signature.TwitterCredentials;

public class Twitter {

    private final String ACCESS_TOKEN;
    private final String ACCESS_TOKEN_SECRET;
    private final String BEARER_TOKEN;
    private final String API_KEY;
    private final String API_SECRET;
    private TwitterClient twitterClient;
    private TwitterApi twitterApi;

    public Twitter() {
        Dotenv env = Dotenv.configure().load();
        ACCESS_TOKEN_SECRET = env.get("ACCESS_TOKEN_SECRET");
        ACCESS_TOKEN = env.get("ACCESS_TOKEN");
        BEARER_TOKEN = env.get("BEARER_TOKEN");
        API_KEY = env.get("API_KEY");
        API_SECRET = env.get("API_SECRET");

        twitterClient = new TwitterClient(TwitterCredentials.builder()
                .apiKey(API_KEY)
                .apiSecretKey(API_SECRET)
                .bearerToken(BEARER_TOKEN)
                .accessToken(ACCESS_TOKEN)
                .accessTokenSecret(ACCESS_TOKEN_SECRET)
                .build()
        );

        twitterApi = new TwitterApi();
        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(BEARER_TOKEN);
        twitterApi.setTwitterCredentials(credentials);
    }

    public TwitterClient getTwitterClient() {
        return twitterClient;
    }

    // こんなんでいけるのか、、？
    public void postTweet() {

    }
}
