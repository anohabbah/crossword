package anoh.kobenan.tp5.util;

import anoh.kobenan.tp5.model.IterateurMots;

public class ControlsArray<E> {
    private E[][] tab;
    private int row, column;

    public ControlsArray(int row, int column) {
        this.row = row;
        this.column = column;
        this.tab = (E[][]) new Object[row][column];
    }

    public void add(int row, int column, E value) {
        assert this.isCorrectIndices(row, column);
        this.tab[row - 1][column - 1] = value;
    }

    public E getValue(int row, int column) {
        assert this.isCorrectIndices(row, column);
        return this.tab[row - 1][column - 1];
    }

    /**
     * Trouve la prochaine valeur non null.
     *
     * @param lig ligne à partir de laquelle commence la recherche
     * @param col colonne à partir de laquelle commence la recherche
     * @param horizontal si {@code True} la recherche s'effectue suivant l'horizontale, sinon suivant la verticale
     * @param next si {@code False} la recherche s'effectue dans la direction opposée
     * @return retourne la prochaine valeur non null suivant la derection et le sens indiqués, null si toutes les
     * valeurs suivantes sont nulls
     */
    public E getNextNotNullValue(int lig, int col, boolean horizontal, boolean next) {
        assert isCorrectIndices(lig, col);

        E value = this.getValue(lig, col);
        if (horizontal) {
            while (col <= getColumn() && value == null) {
                if (next)
                    ++col;
                else
                    --col;
                value = this.getValue(lig, col);
            }
        } else {
            while (lig <= getRow() && value == null) {
                if (next)
                    ++lig;
                else
                    --lig;
                value = this.getValue(lig, col);
            }
        }

        return value;
    }

    private boolean isCorrectIndices(int row, int column) {
        return 1 <= row && row <= getRow() && 1 <= column && column <= getColumn();
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    /**
     * Créer un itérateur soit sur une ligne, soit sur une colonne de tab.
     *
     * @param num        l'indice de la ligne ou la colonne visée.
     * @param horizontal si {@code true} un itérateur sera crée sur la {@code pos}-ième ligne, sinon sur {@code pos}-ième colonne
     * @return un itérateur sur un tableau d'objet {@code E}
     */
    public IterateurMots iterator(boolean horizontal, int num) {
        Object[] tab = null;
        if (horizontal)
            tab = this.tab[num - 1];
        else {
            tab = (E[]) new Object[this.getRow()];
            for (int i = 0; i < this.getRow(); i++) {
                tab[i] = this.getValue(i + 1, num);
            }
        }

        return new IterateurMots(tab);
    }
}
