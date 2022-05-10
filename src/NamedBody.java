import codedraw.CodeDraw;

import java.util.Objects;

public class NamedBody implements Massive {

    private String name;
    private double mass;
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

    // Compares `this` with the specified object. Returns `true` if the specified `o` is not
    // `null` and is of type `NamedBody` and both `this` and `o` have equal names.
    // Otherwise `false` is returned.
    public boolean equals(Object o) {
        return o instanceof NamedBody && (o == this || ((NamedBody)o).name.equals(this.name));
    }

    // Returns the hashCode of `this`.
    @Override
    public int hashCode() {
        return Objects.hash(name, mass, massCenter, currentMovement);
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
