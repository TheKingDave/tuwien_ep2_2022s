import codedraw.CodeDraw;

// A body with a name and an associated force. The leaf node of
// a hierarchical cosmic system. This class implements 'CosmicSystem'.
//
public class NamedBodyForcePair implements CosmicSystem {
    private String name;
    private Body body;
    private Vector3 force = new Vector3();

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBodyForcePair(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;
        this.body = new Body(mass, massCenter, currentMovement);
    }

    // Returns the name of the body.
    public String getName() {
        return name;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public Vector3 getMassCenter() {
        return body.massCenter();
    }

    @Override
    public double getMass() {
        return body.mass();
    }

    @Override
    public int numberOfBodies() {
        return 1;
    }

    @Override
    public double distanceTo(CosmicSystem cs) {
        return cs.getMassCenter().distanceTo(this.body.massCenter());
    }

    @Override
    public void addForceFrom(Body b) {
        if(this.body == b) {
            return;
        }
        this.force = this.force.plus(this.body.gravitationalForce(b));
    }

    @Override
    public void addForceTo(CosmicSystem cs) {
        cs.addForceFrom(this.body);
    }

    @Override
    public BodyLinkedList getBodies() {
        return new BodyLinkedList(this.body);
    }

    @Override
    public void update() {
        body.move(force);
        this.force = new Vector3();
    }

    @Override
    public boolean collidesWith(NamedBodyForcePair b) {
        return this.body.isCollidingWith(b.body);
    }

    @Override
    public void draw(CodeDraw cd) {
        body.draw(cd);
    }

    @Override
    public String toString() {
        return name;
    }
}
