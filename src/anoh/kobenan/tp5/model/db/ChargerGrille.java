package anoh.kobenan.tp5.model.db;

import anoh.kobenan.tp5.model.MotsCroisesTP5;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18018881 on 22/02/2018.
 */
public class ChargerGrille {

    public static final int CHOIX_GRILLE = 10;
    private static final String TABLE_GRILLE = "TP5_GRILLE";
    private static final String TABLE_MOT = "TP5_MOT";

    private Connection conn;

    public ChargerGrille() {
        try {
            if (conn == null)
                conn = connectionMySQL();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection connectionMySQL() throws Exception {
        String url = "jdbc:mysql://anteros.istic.univ-rennes1.fr/base_bousse";
        Class.forName("com.mysql.jdbc.Driver");

        return DriverManager.getConnection(url, "user_18018881", "123456");
    }

    public static MotsCroisesTP5 extraireBD(Connection conn, int grille) throws Exception {
        MotsCroisesTP5 mc = null;

        String sql = "SELECT num_grille, largeur, hauteur FROM " + ChargerGrille.TABLE_GRILLE + " WHERE num_grille = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, grille);
        ResultSet set = stmt.executeQuery();
        if (set.first()) {
            mc = new MotsCroisesTP5(set.getInt("hauteur"), set.getInt("largeur"));

            sql = "SELECT * FROM " + TABLE_MOT + " WHERE num_grille = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, grille);
            ResultSet setMots = stmt.executeQuery();
            while (setMots.next()) {
                int col = setMots.getInt("colonne");
                int lig = setMots.getInt("ligne");

                String solution = setMots.getString("solution");
                boolean horiz = setMots.getBoolean("horizontal");
                mc.setDefinition(lig, col, horiz, setMots.getString("definition"));
                if (horiz) {
                    for (int i = 0; i < solution.length(); i++) {
                        mc.setSolution(lig, col + i, solution.charAt(i));
                    }
                }else {
                    for (int i = 0; i < solution.length(); i++) {
                        mc.setSolution(lig + i, col, solution.charAt(i));
                    }
                }
            }
        }

        return mc;
    }

    public MotsCroisesTP5 extraireGrille(int nomGrille) throws Exception {
        return extraireBD(conn, nomGrille);
    }

    public Map<Integer, String> ListeJeu() {
        Map<Integer, String> table = new HashMap<>();

        String sql = "SELECT * FROM " + ChargerGrille.TABLE_GRILLE;
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet data = stmt.executeQuery(sql);
            while (data.next()) {
                table.put(data.getInt("num_grille"), data.getString("nom_grille"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return table;
    }
}
