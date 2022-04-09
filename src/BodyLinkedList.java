import java.util.Iterator;
import java.util.NoSuchElementException;

// A list of bodies implemented as a linked list.
// The number of elements of the list is not limited.
public class BodyLinkedList implements Iterable<Body> {

    private MyBodyLink head = null;
    private MyBodyLink tail = null;
    private int size = 0;

    // Initializes 'this' as an empty list.
    public BodyLinkedList() {
    }

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public BodyLinkedList(BodyLinkedList list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        this.size = list.size;
        
        MyBodyLink working = list.head;
        MyBodyLink last = null;
        for(int i = 0; i < list.size; i++) {
            MyBodyLink create = new MyBodyLink(working.getBody(), last, null);
            if(last != null) {
                last.setAfter(create);
            }
            last = create;
            if(i == 0) {
                this.head = create;
            } else if(i+1 == list.size) {
                this.tail = create;
            }
            working = working.getAfter();
        }
    }

    private void addFirstBody(Body body) {
        this.head = new MyBodyLink(body, null, null);
        this.tail = head;
        this.size = 1;
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Body body) {
        if (head == null) {
            addFirstBody(body);
            return;
        }
        MyBodyLink oldHead = this.head;
        this.head = new MyBodyLink(body, null, oldHead);
        oldHead.setBefore(this.head);
        this.size++;
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Body body) {
        if (this.tail == null) {
            addFirstBody(body);
            return;
        }
        MyBodyLink oldTail = this.tail;
        this.tail = new MyBodyLink(body, oldTail, null);
        oldTail.setAfter(this.tail);
        this.size++;
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Body getLast() {
        return this.tail == null ? null : this.tail.getBody();
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Body getFirst() {
        return this.head == null ? null : this.head.getBody();
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Body pollFirst() {
        if (this.size == 0) {
            return null;
        }
        if (size == 1) {
            return pollOnly();
        }

        MyBodyLink oldHead = this.head;
        this.head = oldHead.getAfter();
        this.head.setBefore(null);
        this.size--;
        return oldHead.getBody();
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Body pollLast() {
        if (this.size == 0) {
            return null;
        }
        if (this.size == 1) {
            return pollOnly();
        }
        MyBodyLink oldTail = this.tail;
        this.tail = oldTail.getBefore();
        this.tail.setAfter(null);
        this.size--;
        return oldTail.getBody();
    }

    private Body pollOnly() {
        this.size--;
        Body ret = this.head.getBody();
        this.head = null;
        this.tail = null;
        return ret;
    }

    // Inserts the specified element 'body' at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Body body) {
        if (i < 0 || i > size) {
            throw new IllegalArgumentException("index must be between 0 and [size]");
        }

        if (i == 0) {
            addFirst(body);
            return;
        } else if (i == size) {
            addLast(body);
            return;
        }

        MyBodyLink bodyLink = this.head;
        for (int a = 0; a < i - 1; a++) {
            bodyLink = bodyLink.getAfter();
        }

        MyBodyLink newLink = new MyBodyLink(body, bodyLink, bodyLink.getAfter());
        bodyLink.getAfter().setBefore(newLink);
        bodyLink.setAfter(newLink);

        this.size++;
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Body get(int i) {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException("index must be between 0 and [size]");
        }
        MyBodyLink bodyLink = this.head;
        for (int a = 0; a < i; a++) {
            bodyLink = bodyLink.getAfter();
        }
        return bodyLink.getBody();
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Body body) {
        if (size == 0) {
            return -1;
        }
        MyBodyLink bodyLink = this.head;
        int index = 0;
        do {
            if (bodyLink.getBody() == body) {
                return index;
            }
            index++;
            bodyLink = bodyLink.getAfter();
        } while (bodyLink != null);
        return -1;
    }

    // Removes all bodies of this list, which are colliding with the specified
    // body. Returns a list with all the removed bodies.
    public BodyLinkedList removeCollidingWith(Body body) {
        BodyLinkedList ret = new BodyLinkedList();

        MyBodyLink bodyLink = this.head;
        do {
            if (bodyLink.getBody() != body && bodyLink.getBody().isCollidingWith(body)) {
                ret.addLast(bodyLink.getBody());
                if (bodyLink.getBefore() == null) {
                    this.pollFirst();
                } else if (bodyLink.getAfter() == null) {
                    this.pollLast();
                } else {
                    bodyLink.getBefore().setAfter(bodyLink.getAfter());
                    bodyLink.getAfter().setBefore(bodyLink.getBefore());
                    this.size--;
                }
            }
            bodyLink = bodyLink.getAfter();
        } while (bodyLink != null);

        return ret;
    }

    // Returns the number of bodies in this list.
    public int size() {
        return size;
    }

    @Override
    public Iterator<Body> iterator() {
        return new BodyLinkedListIterator(this.head);
    }

    private class BodyLinkedListIterator implements Iterator<Body> {
        private MyBodyLink bodyLink;

        public BodyLinkedListIterator(MyBodyLink bodyLink) {
            this.bodyLink = bodyLink;
        }

        @Override
        public boolean hasNext() {
            return bodyLink != null;
        }

        @Override
        public Body next() {
            if (bodyLink == null) {
                throw new NoSuchElementException();
            }
            Body ret = bodyLink.getBody();
            bodyLink = bodyLink.getAfter();
            return ret;
        }

        @Override
        public void remove() {
            Iterator.super.remove();
        }
    }
}
