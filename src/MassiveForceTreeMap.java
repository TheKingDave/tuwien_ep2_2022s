import codedraw.CodeDraw;

import java.util.Arrays;


// A map that associates an object of 'Massive' with a Vector3. The number of key-value pairs
// is not limited.
//
public class MassiveForceTreeMap {

    MassiveTreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {
        if(root == null) {
            root = new MassiveTreeNode(key, value);
            return null;
        }
        return root.put(key, value);
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {
        return root == null ? null : root.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    //Precondition: key != null
    public boolean containsKey(Massive key) {
        return root != null && root.contains(key);
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to 'key.getMass()'.
    public String toString() {
        return root.toString();
    }

    // Returns a `MassiveSet` view of the keys contained in this tree map. Changing the
    // elements of the returned `MassiveSet` object also affects the keys in this tree map.
    public MassiveSet getKeys() {
        _MassiveSet ms = new _MassiveSet(this);
        this.root.addKeysToList(ms);
        return ms;
    }

    public void remove(Massive element) {
        if(this.root != null) this.root.remove(element);
    }
}

class MassiveTreeNode {
    private final Massive massive;
    private final double mass;
    private Vector3 value;
    private MassiveTreeNode left, right;

    MassiveTreeNode(Massive massive, Vector3 value) {
        this.massive = massive;
        this.value = value;
        this.mass = massive.mass();
    }
    
    public Vector3 put(Massive key, Vector3 value) {
        if(key == this.massive) {
            Vector3 old = this.value;
            this.value = value;
            return old;
        }
        if(key.mass() < this.mass) {
            if(left == null) {
                left = new MassiveTreeNode(key, value);
                return null;
            }
            return left.put(key, value);
        } else {
            if(right == null) {
                right = new MassiveTreeNode(key, value);
                return null;
            }
            return right.put(key, value);
        }
    }
    
    public Vector3 get(Massive key) {
        if(key == this.massive) {
            return value;
        }
        MassiveTreeNode n = key.mass() < this.mass ? this.left : this.right;
        return n == null ? null : n.get(key);
    }

    public boolean contains(Massive key) {
        if(key == this.massive) {
            return true;
        }
        MassiveTreeNode n = key.mass() < this.mass ? this.left : this.right;
        return n != null && n.contains(key);
    }
    
    public void addKeysToList(_MassiveSet set) {
        if(this.left != null) this.left.addKeysToList(set);
        set.add(this.massive);
        if(this.right != null) this.right.addKeysToList(set);
    }
    
    public void remove(Massive key) {
        if(this.mass > key.mass()) {
            if(right != null) {
                right.remove(key);
            }
        } else {
            if(left != null) {
                left.remove(key);
            }
            if(this.mass == key.mass()) {
                if(left == null) {
                    return;
                }
                MassiveTreeNode p = left;
                while(p.right != null) {
                    p = p.right;
                }
                p.right = right;
            }
        }
    }

    @Override
    public String toString() {
        String ret = "";
        if(left != null) ret += left.toString();
        ret += this.massive.toString() + "\n";
        if(right != null) ret += right.toString();
        return ret;
    }
}

class _MassiveSet implements MassiveSet {
    MassiveForceTreeMap massiveForceTreeMap;
    Massive[] massives = new Massive[10];
    int size = 0;

    public _MassiveSet(MassiveForceTreeMap massiveForceTreeMap) {
        this.massiveForceTreeMap = massiveForceTreeMap;
    }

    @Override
    public void draw(CodeDraw cd) {
        for(Massive m : massives) {
            m.draw(cd);
        }
    }

    @Override
    public boolean contains(Massive element) {
        for(int i = 0; i < this.size; i++) {
            if(massives[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove(Massive element) {
        if(this.size <= 0) return;
        
        int index = -1;
        for(int i = 0; i < this.size; i++) {
            if(massives[i].equals(element)) {
                index = i;
                break;
            }
        }
        
        if(index < 0) return;

        System.arraycopy(massives, index, massives, index-1, size - index - 1);
        this.massiveForceTreeMap.remove(element);
        
        this.massives[size-1] = null;
        this.size--;
    }

    @Override
    public void clear() {
        for(int i = 0; i < this.size; i++) {
            this.massiveForceTreeMap.remove(this.massives[i]);
        }
        this.massives = new Massive[10];
        this.size = 0;
    }
    
    public void add(Massive massive) {
        if(this.contains(massive)) {
            return;
        }
        if (size + 1 >= massives.length) {
            massives = Arrays.copyOf(massives, massives.length * 2);
        }
        massives[size++] = massive;
    }
    
    public Massive get(int index) {
        if(index < this.size) {
            return massives[index];
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MassiveIterator iterator() {
        return new _MassiveIterator(this);
    }

    @Override
    public MassiveLinkedList toList() {
        MassiveLinkedList list = new MassiveLinkedList();
        for(int i = 0; i < this.size; i++) {
            list.addLast(massives[i].copy());
        }
        return list;
    }
}

class _MassiveIterator implements MassiveIterator {
    private final _MassiveSet src;
    private int i = 0;

    public _MassiveIterator(_MassiveSet src) {
        this.src = src;
    }

    @Override
    public Massive next() {
        return src.get(i++);
    }

    @Override
    public boolean hasNext() {
        return this.i < this.src.size();
    }
}