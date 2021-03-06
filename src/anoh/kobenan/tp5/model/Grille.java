package anoh.kobenan.tp5.model;

public class Grille<T> {
    private int hauteur, largeur;
    protected Object[][] cellules;

    @SuppressWarnings("unchecked")
    public Grille(int hauteur, int largeur) {
        assert hauteur >= 0;
        assert largeur >= 0;
        this.hauteur = hauteur;
        this.largeur = largeur;
        cellules = (T[][]) new Object[hauteur][largeur];
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public boolean coordCorrectes(int lig, int col) {
        return 1 <= lig && lig <= getHauteur()
                && 1 <= col && col <= getLargeur();
    }

    @SuppressWarnings("unchecked")
    public T getCellule(int lig, int col) {
        assert coordCorrectes(lig, col);
        return (T) cellules[lig - 1][col - 1];
    }

    public void setCellule(int lig, int col, T ch) {
        assert coordCorrectes(lig, col);
        cellules[lig - 1][col - 1] = ch;
    }

    @Override
    public String toString() {
        StringBuffer resultat = new StringBuffer();
        for (int l = 1; l <= this.getHauteur(); l++) {
            for (int c = 1; c <= this.getLargeur(); c++) {
                if (c > 1) {
                    resultat.append('|');
                }
                resultat.append(this.getCellule(l, c));
            }
            resultat.append('\n');
        }
        return resultat.toString();
    }

    public IterateurMots iterateurMots(boolean horizontal, int num) {
        Object[] tab = null;
        if (horizontal)
            tab = this.cellules[num];
        else {
            tab = new Object[this.getHauteur()];
            for (int i = 0; i < this.getHauteur(); i++) {
                tab[i] = this.getCellule(i+1, num);
            }
        }

        return new IterateurMots(tab);
    }

    public static void main(String[] args) {
        Grille<String> maGrille = new Grille<String>(3, 5);
        for (int l = 1; l <= maGrille.getHauteur(); l++) {
            String texteLigne = Integer.toString(l);
            for (int c = 1; c <= maGrille.getLargeur(); c++) {
                maGrille.setCellule(l, c, texteLigne + ',' + Integer.toString(c));
            }
        }
        System.out.println(maGrille);
    }
}
