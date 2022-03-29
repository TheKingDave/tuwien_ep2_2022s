// A list of bodies implemented as a linked list.
// The number of elements of the list is not limited.
public class BodyLinkedList {

    //TODO: declare variables.
    private MyBodyLink head = null;
    private MyBodyLink tail = null;

    // Initializes 'this' as an empty list.
    public BodyLinkedList() {}

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public BodyLinkedList(BodyLinkedList list) {
        if(list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        this.head = list.head;
        this.tail = list.tail;
    }

    private void addFirstBody(Body body) {
        this.head = new MyBodyLink(body, null, null);
        this.tail = head;
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Body body) {
        if(head == null) {
            addFirstBody(body);
            return;
        }
        this.head = new MyBodyLink(body, null, this.head);
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Body body) {
        if(this.tail == null) {
            addFirstBody(body);
            return;
        }
        this.tail = new MyBodyLink(body, this.tail, null);
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
        if(this.head == null) {
            return null;
        }
        MyBodyLink oldHead = this.head;
        this.head = oldHead.getAfter();
        return oldHead.getBody();
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Body pollLast() {
        if(this.tail == null) {
            return null;
        }
        MyBodyLink oldTail = this.tail;
        this.tail = oldTail.getBefore();
        return oldTail.getBody();
    }

    // Inserts the specified element 'body' at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Body body) {

        //TODO: implement method.
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Body get(int i) {

        //TODO: implement method.
        return null;
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Body body) {

        //TODO: implement method.
        return -2;
    }

    // Removes all bodies of this list, which are colliding with the specified
    // body. Returns a list with all the removed bodies.
    public BodyLinkedList removeCollidingWith(Body body) {

        //TODO: implement method.
        return null;
    }

    // Returns the number of bodies in this list.
    public int size() {

        //TODO: implement method.
        return -1;
    }
}
