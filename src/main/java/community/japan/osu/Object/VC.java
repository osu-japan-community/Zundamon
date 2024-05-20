package community.japan.osu.Object;

import io.github.cdimascio.dotenv.Dotenv;

public class VC {

    private int id;
    private long VC_CHANNEL;
    private long VC_TEXT;
    private boolean isVC;

    public VC () {
        Dotenv dotenv = Dotenv.configure()
                .load();

        VC_CHANNEL = Long.parseLong(dotenv.get("VC_CHANNEL"));
        VC_TEXT = Long.parseLong(dotenv.get("VC_TEXT"));

        id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getVC() {
        return isVC;
    }

    public void setVC(boolean isVC) {
        this.isVC = isVC;
    }

    public long getVC_CHANNEL() {
        return VC_CHANNEL;
    }

    public long getVC_TEXT() {
        return VC_TEXT;
    }
}
