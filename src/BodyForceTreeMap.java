import javax.swing.tree.TreeNode;

// A map that associates a Body with a Vector3 (typically this is the force exerted on the body).
// The number of key-value pairs is not limited.
public class BodyForceTreeMap {

    //TODO: declare variables.
    TreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 value) {
        if (root == null) {
            root = new TreeNode(key, value);
            return null;
        }
        return root.add(key, value);
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        return root.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Body key) {
        return get(key) != null;
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to the mass of the bodies.
    public String toString() {

        //TODO: implement method.
        return null;

    }

    private static class TreeNode {
        private final double mass;
        private Vector3 value;
        private TreeNode left, right;

        public TreeNode(Body body, Vector3 value) {
            this.mass = body.mass();
            this.value = value;
        }

        public Vector3 get(Body body) {
            if (body.mass() == mass) {
                return value;
            } else if (body.mass() < mass && left != null) {
                return left.get(body);
            } else if (body.mass() > mass && right != null) {
                return right.get(body);
            }
            return null;
        }

        public Vector3 add(Body body, Vector3 value) {
            if (body.mass() == this.mass) {
                Vector3 ret = this.value;
                this.value = value;
                return ret;
            } else if (body.mass() < this.mass) {
                if (left != null) {
                    return left.add(body, value);
                }
                left = new TreeNode(body, value);
            } else if (body.mass() > this.mass) {
                if (right != null) {
                    return right.add(body, value);
                }
                right = new TreeNode(body, value);
            }
            return null;
        }
    }
}
