import javax.swing.tree.TreeNode;

// A hash map that associates a 'Massive'-object with a Vector3 (typically this is the force
// exerted on the object). The number of key-value pairs is not limited.
//
public class MassiveForceHashMap {

    private MassiveNode root;

    // Initializes 'this' as an empty map.
    public MassiveForceHashMap() {
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        if (root == null) {
            root = new MassiveNode(key, value);
        }
        return root.add(key, value);
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        return root.get(key);
    }

    public int size() {
        return root.count();
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Massive key) {
        return get(key) != null;
    }

    // Returns a readable representation of this map, with all key-value pairs. Their order is not
    // defined.
    public String toString() {
        return root.generateString();
    }

    // Compares `this` with the specified object for equality. Returns `true` if the specified
    // `o` is not `null` and is of type `MassiveForceHashMap` and both `this` and `o` have equal
    // key-value pairs, i.e. the number of key-value pairs is the same in both maps and every
    // key-value pair in `this` equals one key-value pair in `o`. Two key-value pairs are
    // equal if the two keys are equal and the two values are equal. Otherwise `false` is returned.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MassiveForceHashMap map = (MassiveForceHashMap) o;

        if (this.size() != map.size()) return false;
        
        for(Massive m : this.keyList()) {
            if (!get(m).equals(map.get(m))) {
                return false;
            }
        }

        return true;
    }

    // Returns the hashCode of `this`.
    public int hashCode() {

        //TODO: implement method.
        return 0;
    }


    // Returns a list of all the keys in no specified order.
    public MassiveLinkedList keyList() {
        MassiveLinkedList list = new MassiveLinkedList();
        root.list(list);
        return list;
    }

    private static class MassiveNode {
        private final Massive massive;
        private final int hash;
        private Vector3 value;
        private MassiveNode left, right;

        public MassiveNode(Massive massive, Vector3 value) {
            this.massive = massive;
            this.hash = massive.hashCode();
            this.value = value;
        }

        public Vector3 get(Massive massive) {
            if (this.massive.equals(massive)) {
                return value;
            } else if (massive.hashCode() < hash && left != null) {
                return left.get(massive);
            } else if (massive.hashCode() > hash && right != null) {
                return right.get(massive);
            }
            return null;
        }

        public Vector3 add(Massive massive, Vector3 value) {
            if (this.massive.equals(massive)) {
                Vector3 ret = this.value;
                this.value = value;
                return ret;
            } else if (massive.hashCode() < this.hash) {
                if (left != null) {
                    return left.add(massive, value);
                }
                left = new MassiveNode(massive, value);
            } else if (massive.hashCode() > this.hash) {
                if (right != null) {
                    return right.add(massive, value);
                }
                right = new MassiveNode(massive, value);
            }
            return null;
        }

        public int count() {
            int count = 1;
            if (right != null) count += right.count();
            if (left != null) count += left.count();
            return count;
        }

        public void list(MassiveLinkedList list) {
            if (right != null) right.list(list);
            list.addLast(this.massive);
            if (left != null) left.list(list);
        }

        public String generateString() {
            String ret = "";

            if (right != null) ret += right.generateString();
            ret += massive + ": " + value + "\n";
            if (left != null) ret += left.generateString();

            return ret;
        }
    }
    
    

}