import java.util.Arrays;

// A queue of bodies. A collection designed for holding bodies prior to processing.
// The bodies of the queue can be accessed in a FIFO (first-in-first-out) manner,
// i.e., the body that was first inserted by 'add' is retrieved first by 'poll'.
// The number of elements of the queue is not limited.
//
public class BodyQueue {

    private int size;
    private Body[] bodies;

    // Initializes this queue with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial Size must be bigger than 0");
        }
        this.bodies = new Body[initialCapacity];
        this.size = 0;
    }

    // Initializes this queue as an independent copy of the specified queue.
    // Calling methods of this queue will not affect the specified queue
    // and vice versa.
    // Precondition: q != null.
    public BodyQueue(BodyQueue q) {
        if (q == null) {
            throw new IllegalArgumentException("q must not be null");
        }
        size = q.size;
        bodies = Arrays.copyOf(q.bodies, q.size);
    }

    // Adds the specified body 'b' to this queue.
    public void add(Body b) {
        if (size + 1 >= bodies.length) {
            bodies = Arrays.copyOf(bodies, bodies.length * 2);
        }
        bodies[this.size] = b;
        size++;
    }

    // Retrieves and removes the head of this queue, or returns 'null'
    // if this queue is empty.
    public Body poll() {
        if (this.size <= 0) {
            return null;
        }
        Body ret = bodies[0];

        System.arraycopy(bodies, 1, bodies, 0, size - 1);
        
        this.size--;
        return ret;
    }

    // Returns the number of bodies in this queue.
    public int size() {
        return size;
    }
}
