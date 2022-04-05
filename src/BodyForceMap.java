import java.util.Arrays;

// A map that associates a body with a force exerted on it. The number of
// key-value pairs is not limited.
//
public class BodyForceMap {

    MyBodyVectorPair[] pairs;
    int size = 0;

    // Initializes this map with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyForceMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("InitialCapacity must be bigger than 0");
        }
        pairs = new MyBodyVectorPair[initialCapacity];
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 force) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        if (size + 1 > pairs.length) {
            pairs = Arrays.copyOf(pairs, pairs.length * 2);
        }

        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + ((right - left) / 2);
            MyBodyVectorPair m = pairs[middle];
            if (m.getKey() == key) {
                Vector3 ret = m.getValue();
                m.setValue(force);
                return ret;
            }
            if (m.getKey().mass() < key.mass()) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        int index = right + 1;
        System.arraycopy(pairs, index, pairs, index + 1, size - index);
        pairs[index] = new MyBodyVectorPair(key, force);
        size++;
        return null;
    }

    // Returns the value associated with the specified key, i.e. the returns the force vector
    // associated with the specified body. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int middle = left + ((right - left) / 2);
            MyBodyVectorPair m = pairs[middle];
            if (m.getKey() == key) {
                return pairs[middle].getValue();
            }
            if (m.getKey().mass() < key.mass()) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return null;
    }

    public BodyQueue getKeys() {
        BodyQueue queue = new BodyQueue(size);
        System.out.println("Size: " + size);
        for(int i = size-1; i >= 0; i--) {
            queue.add(pairs[i].getKey());
        }
        System.out.println(queue.size());
        return queue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < this.size; i++) {
            MyBodyVectorPair pair = pairs[i];
            builder.append(pair.getKey().toString());
            builder.append(": ");
            builder.append(pair.getValue().toString());
            builder.append("\n");
        }

        return builder.toString();
    }
}
