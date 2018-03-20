package anoh.kobenan.tp5.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterateurMots implements Iterator<Object> {
    private Object[] tab;
    private int cursor;

    public IterateurMots(Object[] tab) {
        this.tab = tab;

        this.cursor = 0;
        this.updateCursor();
    }

    private void updateCursor() {
        while (this.cursor < this.tab.length && this.tab[this.cursor] == null) {
            this.cursor++;
        }
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return this.cursor < this.tab.length && this.tab[this.cursor] != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Object next() {
        assert this.hasNext() : "Itérateur vide";

        Object next = (Object) this.tab[this.cursor];

        ++this.cursor;
        this.updateCursor();

        return next;
    }
}
