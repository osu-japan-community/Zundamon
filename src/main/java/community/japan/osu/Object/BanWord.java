package community.japan.osu.Object;

import community.japan.osu.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanWord {

    List<String> banWords = new ArrayList<>();

    public List<String> getBanWords() {
        return banWords;
    }

    public void loadBanWord() {

        Connection connection = Main.bot.getConnection();
        PreparedStatement ps;
        ResultSet result;

        try {

            ps = connection.prepareStatement("select * from banword");
            result = ps.executeQuery();
            while (result.next()) {
                banWords.add(result.getString("content"));
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
