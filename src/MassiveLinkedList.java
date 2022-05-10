import java.util.Iterator;
import java.util.NoSuchElementException;

// A list of massive objects implemented as a linked list.
// The number of elements of the list is not limited.
public class MassiveLinkedList implements Iterable<Massive> {

    private MassiveLink head = null;
    private MassiveLink tail = null;
    private int size = 0;

    // Initializes 'this' as an empty list.
    public MassiveLinkedList() {}

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public MassiveLinkedList(MassiveLinkedList list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        this.size = list.size;

        MassiveLink working = list.head;
        MassiveLink last = null;
        for (int i = 0; i < list.size; i++) {
            MassiveLink create = new MassiveLink(working.getMassive(), last, null);
            if (last != null) {
                last.setAfter(create);
            }
            last = create;
            if (i == 0) {
                this.head = create;
            } else if (i + 1 == list.size) {
                this.tail = create;
            }
            working = working.getAfter();
        }
    }

    private void addFirstMassive(Massive massive) {
        this.head = new MassiveLink(massive, null, null);
        this.tail = head;
        this.size = 1;
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Massive massive) {
        if (head == null) {
            addFirstMassive(massive);
            return;
        }
        MassiveLink oldHead = this.head;
        this.head = new MassiveLink(massive, null, oldHead);
        oldHead.setBefore(this.head);
        this.size++;
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Massive massive) {
        if (this.tail == null) {
            addFirstMassive(massive);
            return;
        }
        MassiveLink oldTail = this.tail;
        this.tail = new MassiveLink(massive, oldTail, null);
        oldTail.setAfter(this.tail);
        this.size++;
    }

    // Add all bodies from otherList, otherList gets destroyed by this function
    public void addAllLast(MassiveLinkedList otherList) {
        if (this.size == 0) {
            this.head = otherList.head;
            this.tail = otherList.tail;
            this.size = otherList.size;
        } else {
            this.tail.setAfter(otherList.head);
            this.tail = otherList.tail;
            this.size += otherList.size;
        }
        otherList.head = null;
        otherList.tail = null;
        otherList.size = 0;
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive getLast() {
        return this.tail == null ? null : this.tail.getMassive();
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive getFirst() {
        return this.head == null ? null : this.head.getMassive();
    }

    private Massive pollOnly() {
        this.size--;
        Massive ret = this.head.getMassive();
        this.head = null;
        this.tail = null;
        return ret;
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollFirst() {
        if (this.size == 0) {
            return null;
        }
        if (size == 1) {
            return pollOnly();
        }

        MassiveLink oldHead = this.head;
        this.head = oldHead.getAfter();
        this.head.setBefore(null);
        this.size--;
        return oldHead.getMassive();
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollLast() {
        if (this.size == 0) {
            return null;
        }
        if (this.size == 1) {
            return pollOnly();
        }
        MassiveLink oldTail = this.tail;
        this.tail = oldTail.getBefore();
        this.tail.setAfter(null);
        this.size--;
        return oldTail.getMassive();
    }

    // Inserts the specified element at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Massive m) {
        if (i < 0 || i > size) {
            throw new IllegalArgumentException("index must be between 0 and [size]");
        }

        if (i == 0) {
            addFirst(m);
            return;
        } else if (i == size) {
            addLast(m);
            return;
        }

        MassiveLink bodyLink = this.head;
        for (int a = 0; a < i - 1; a++) {
            bodyLink = bodyLink.getAfter();
        }

        MassiveLink newLink = new MassiveLink(m, bodyLink, bodyLink.getAfter());
        bodyLink.getAfter().setBefore(newLink);
        bodyLink.setAfter(newLink);

        this.size++;
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Massive get(int i) {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("index must be between 0 and [size]");
        }
        MassiveLink bodyLink = this.head;
        for (int a = 0; a < i; a++) {
            bodyLink = bodyLink.getAfter();
        }
        return bodyLink.getMassive();
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Massive m) {
        if (size == 0) {
            return -1;
        }
        MassiveLink bodyLink = this.head;
        int index = 0;
        do {
            if (bodyLink.getMassive().equals(m)) {
                return index;
            }
            index++;
            bodyLink = bodyLink.getAfter();
        } while (bodyLink != null);
        return -1;
    }

    // Returns the number of elements in this list.
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Massive> iterator() {
        return new MassiveLinkedListIterator(this.head);
    }

    private class MassiveLinkedListIterator implements Iterator<Massive> {
        private MassiveLink massiveLink;

        public MassiveLinkedListIterator(MassiveLink massiveLink) {
            this.massiveLink = massiveLink;
        }

        @Override
        public boolean hasNext() {
            return massiveLink != null && (!massiveLink.shouldSkip() || massiveLink.getAfter() != null);
        }

        @Override
        public Massive next() {
            if (massiveLink.shouldSkip()) {
                massiveLink = massiveLink.getAfter();
            }
            if (massiveLink == null) {
                throw new NoSuchElementException();
            }
            Massive ret = massiveLink.getMassive();
            massiveLink = massiveLink.getAfter();
            return ret;
        }

        @Override
        public void remove() {
            Iterator.super.remove();
        }
    }

    private static class MassiveLink {
        private MassiveLink before;
        private MassiveLink after;
        private Massive massive;
        private boolean skip = false;

        public MassiveLink(Massive massive, MassiveLink before, MassiveLink after) {
            this.before = before;
            this.after = after;
            this.massive = massive;
        }

        public MassiveLink getBefore() {
            return before;
        }

        public void setBefore(MassiveLink before) {
            this.before = before;
        }

        public MassiveLink getAfter() {
            return after;
        }

        public void setAfter(MassiveLink after) {
            this.after = after;
        }

        public Massive getMassive() {
            return massive;
        }

        public void setMassive(Body massive) {
            this.massive = massive;
        }

        public void skip() {
            this.skip = true;
        }

        public boolean shouldSkip() {
            return this.skip;
        }
    }
}