public class MyBodyVectorPair {
    private Body key;
    private Vector3 value;

    public MyBodyVectorPair(Body key, Vector3 value) {
        this.key = key;
        this.value = value;
    }

    public Body getKey() {
        return key;
    }

    public Vector3 getValue() {
        return value;
    }

    public void setKey(Body key) {
        this.key = key;
    }

    public void setValue(Vector3 value) {
        this.value = value;
    }
}
