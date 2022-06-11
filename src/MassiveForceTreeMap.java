import codedraw.CodeDraw;

import java.util.Arrays;
import java.util.NoSuchElementException;


// A map that associates an object of 'Massive' with a Vector3. The number of key-value pairs
// is not limited.
//

public class MassiveForceTreeMap {

    private MyMassiveTreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        if (root == null) {
            root = new MyMassiveTreeNode(key, value, null, null);
            return null;
        }
        return root.add(key, value);
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        if (!containsKey(key)) {
            return null;
        }
        return root.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    //Precondition: key != null
    public boolean containsKey(Massive key) {
        if (root == null) {
            return false;
        }
        return root.containsKey(key);
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to 'key.getMass()'.
    public String toString() {
        if (root == null) {
            return "";
        }
        return root.toString();
    }

    // Returns a `MassiveSet` view of the keys contained in this tree map. Changing the
    // elements of the returned `MassiveSet` object also affects the keys in this tree map.
    public MassiveSet getKeys() {

        // TODO: implement method.
        return new MyMassiveSet(this, root);
    }

    public int getSize() {
        if (root == null) return 0;
        return root.size();
    }

    public void remove(Massive key) {
        if (root != null) {
            if (root.key().equals(key)) {
                //special case: root needs to be removed
                MyMassiveTreeNode dummyRoot = new MyMassiveTreeNode(null, null, root, null);
                root.remove(key, dummyRoot);
                root = dummyRoot.left();
            } else {
                root.remove(key, null);
            }
        }
    }

    public void clear() {
        root = null;
    }


    //additional method to visualize tree
    public void drawTree() {
        CodeDraw cd = new CodeDraw(1000, 500);
        if (root == null) {
            cd.drawText(200, 50, "Empty tree");
        } else {
            root.draw(cd, 500, 10);
        }
        cd.show();
    }
}

class MyMassiveTreeNode {
    private MyMassiveTreeNode left;
    private MyMassiveTreeNode right;
    private Massive key;
    private Vector3 value;

    public MyMassiveTreeNode(Massive key, Vector3 value, MyMassiveTreeNode left, MyMassiveTreeNode right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Vector3 add(Massive key, Vector3 value) {
        if (key == this.key) {
            Vector3 oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        if (key.mass() < this.key.mass()) {
            if (left == null) {
                left = new MyMassiveTreeNode(key, value, null, null);
                return null;
            } else {
                return left.add(key, value);
            }
        } else {
            if (right == null) {
                right = new MyMassiveTreeNode(key, value, null, null);
                return null;
            } else {
                return right.add(key, value);
            }
        }
    }

    public Vector3 get(Massive key) {
        if (key == this.key) {
            return value;
        }

        if (key.mass() < this.key.mass()) {
            if (left == null) {
                return null;
            }
            return left.get(key);
        } else {
            if (right == null) {
                return null;
            }
            return right.get(key);
        }

    }

    public String toString() {
        String result;
        result = right == null ? "" : right.toString();
        result += "(" + this.key + "|" + this.value + ")\n";
        result += left == null ? "" : left.toString();
        return result;
    }

    public boolean containsKey(Massive key) {
        if (key.equals(this.key)) {
            return true;
        }
        if (key.mass() < this.key.mass()) {
            if (left == null) {
                return false;
            }
            return left.containsKey(key);
        } else {
            if (right == null) {
                return false;
            }
            return right.containsKey(key);
        }
    }

    public int size() {
        int sum = 1;
        if (left != null) sum += left.size();
        if (right != null) sum += right.size();
        return sum;
    }

    public MyMassiveTreeNode left() {
        return left;
    }

    public MyMassiveTreeNode right() {
        return right;
    }

    public MyMassiveTreeIterator iterator() {
        return new MyMassiveTreeIterator(this);
    }

    public Massive key() {
        return key;
    }

    public void remove(Massive key, MyMassiveTreeNode parent) {
        if (key.mass() < this.key.mass()) {
            if (left != null) {
                left.remove(key, this);
            }
        } else if (key.mass() > this.key.mass()) {
            if (right != null) {
                right.remove(key, this);
            }
        } else {
            if (left != null && right != null) {
                this.key = right.minKey();
                right.remove(this.key, this);
            } else if (parent.left == this) {
                parent.left = (left != null) ? left : right;
            } else if (parent.right == this) {
                parent.right = (left != null) ? left : right;
            }
        }
    }

    public void draw(CodeDraw cd, double x, double y) {
        cd.fillCircle(x, y, 10);
        cd.getTextFormat().setFontSize(12);
        cd.drawText(x + 10, y, key.toString());
        if (left != null) {
            cd.drawLine(x, y, x - 60, y + 60);
            left.draw(cd, x - 60, y + 60);
        }
        if (right != null) {
            cd.drawLine(x, y, x + 60, y + 60);
            right.draw(cd, x + 60, y + 60);
        }
    }

    private Massive minKey() {
        if (left == null) {
            return this.key;
        }
        return left.minKey();
    }

}

class MyMassiveSet implements MassiveSet {

    private MassiveForceTreeMap map;
    private MyMassiveTreeNode root;

    public MyMassiveSet(MassiveForceTreeMap map, MyMassiveTreeNode root) {
        this.map = map;
        this.root = root;
    }

    @Override
    public void draw(CodeDraw cd) {
        for (Massive m : this) {
            m.draw(cd);
        }
    }

    @Override
    public MassiveIterator iterator() {
        return new MyMassiveTreeIterator(root);
    }

    @Override
    public boolean contains(Massive element) {
        return map.containsKey(element);
    }

    @Override
    public void remove(Massive element) {
        map.remove(element);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.getSize();
    }

    @Override
    public MassiveLinkedList toList() {
        MassiveLinkedList list = new MassiveLinkedList();
        for (Massive m : this) {
            list.addLast(m);
        }
        return list;
    }
}

class MyMassiveTreeIterator implements MassiveIterator {

    MyMassiveTreeNode node;
    MyMassiveTreeIterator iterLeft, iterRight;

    public MyMassiveTreeIterator(MyMassiveTreeNode node) {
        this.node = node;
        if (node != null) {
            if (node.left() != null) {
                iterLeft = node.left().iterator();
            }
            if (node.right() != null) {
                iterRight = node.right().iterator();
            }
        }
    }


    @Override
    public Massive next() {
        if (!hasNext()) {
            throw new IllegalStateException();
        }
        if (node != null) {
            Massive toReturn = node.key();
            node = null;
            return toReturn;
        }
        if (iterLeft != null && iterLeft.hasNext()) {
            return iterLeft.next();
        }
        return iterRight.next();
    }

    @Override
    public boolean hasNext() {
        return (node != null || (iterLeft != null && iterLeft.hasNext()) || (iterRight != null && iterRight.hasNext()));
    }

    @Override
    public void remove() {
        
    }
}
