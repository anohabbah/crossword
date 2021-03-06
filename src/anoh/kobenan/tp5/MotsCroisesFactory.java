package anoh.kobenan.tp5;

import anoh.kobenan.tp5.model.MotsCroisesTP5;

public class MotsCroisesFactory {

    public static MotsCroisesTP5 creerMotsCroises2x3() {
        MotsCroisesTP5 mc = new MotsCroisesTP5(2, 3);
        for (int i = 1; i <= mc.getHauteur(); i++)
            for (int j = 1; j <= mc.getLargeur(); j++) {
                mc.setCaseNoire(i, j, false);
            }
        mc.setCaseNoire(2, 2, true);
        mc.setDefinition(1, 1, true, "Note");
        mc.setSolution(1, 1, 'S');
        mc.setSolution(1, 2, 'O');
        mc.setSolution(1, 3, 'L');
        mc.setDefinition(1, 1, false, "Autre note");
        mc.setSolution(2, 1, 'I');
        mc.setDefinition(1, 3, false, "Et encore une note");
        mc.setSolution(2, 3, 'A');
        return mc;
    }
}
