package community.japan.osu.Object;

import io.github.cdimascio.dotenv.Dotenv;

public class VoiceChat {

    int id = 0;
    int speaker;
    long VC_TEXT;
    long VC_CHANNEL;
    long[] YOMIAGE_BOT_ID = new long[] {727508841368911943L, 533698325203910668L, 472911936951156740L};
    boolean inVoiceChannel = false;

    public VoiceChat () {
        Dotenv env = Dotenv.configure().load();
        VC_CHANNEL = Long.parseLong(env.get("VC_CHANNEL"));
        VC_TEXT = Long.parseLong(env.get("VC_TEXT"));
        // zundamon
        speaker = 1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInVoiceChannel(boolean inVoiceChannel) {
        this.inVoiceChannel = inVoiceChannel;
    }

    public void setSpeaker(int speaker) {
        this.speaker = speaker;
    }

    public int getId() {
        return id;
    }

    public int getSpeaker() {
        return speaker;
    }

    public long getVC_TEXT() {
        return VC_TEXT;
    }

    public long[] getYOMIAGE_BOT_ID() {
        return YOMIAGE_BOT_ID;
    }

    public long getVC_CHANNEL() {
        return VC_CHANNEL;
    }

    public boolean isInVoiceChannel() {
        return inVoiceChannel;
    }
}
