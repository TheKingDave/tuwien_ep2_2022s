import codedraw.CodeDraw;

public class NamedBody implements Massive {

    private final String name;
    private final double mass;
    private Vector3 massCenter;
    private Vector3 currentMovement;

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBody(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;
        this.mass = mass;
        this.massCenter = massCenter;
        this.currentMovement = currentMovement;
    }

    // Returns the name of the body.
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedBody namedBody = (NamedBody) o;

        return name.equals(namedBody.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    // Returns a readable representation including the name of this body.
    public String toString() {
        return this.name;
    }


    @Override
    public void draw(CodeDraw cd) {
        cd.setColor(SpaceDraw.massToColor(mass));
        this.massCenter.drawAsFilledCircle(cd, radius());
    }

    @Override
    public double getMass() {
        return this.mass;
    }

    @Override
    public Vector3 getMassCenter() {
        return this.massCenter;
    }

    @Override
    public void move(Vector3 force) {
        Vector3 newPos = force.times(1 / mass).plus(massCenter).plus(currentMovement);
        Vector3 newMov = newPos.minus(massCenter);

        massCenter = newPos;
        currentMovement = newMov;
    }
}
