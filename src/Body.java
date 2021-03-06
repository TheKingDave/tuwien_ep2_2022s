import codedraw.CodeDraw;

import java.util.Objects;

// This class represents celestial bodies like stars, planets, asteroids, etc..
public class Body implements Massive {

    private double mass;
    private Vector3 massCenter; // position of the mass center.
    private Vector3 currentMovement;


    public Body(double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.mass = mass;
        this.massCenter = massCenter;
        this.currentMovement = currentMovement;
    }

    public Body(Body body) {
        this(body.mass, new Vector3(body.massCenter), new Vector3(body.currentMovement));
    }

    // Returns the distance between the mass centers of this body and the specified body 'b'.
    public double distanceTo(Body b) {
        return this.massCenter.distanceTo(b.massCenter);
    }

    // Returns a vector representing the gravitational force exerted by 'b' on this body.
    // The gravitational Force F is calculated by F = G*(m1*m2)/(r*r), with m1 and m2 being the
    // masses of the objects interacting, r being the distance between the centers of the masses
    // and G being the gravitational constant.
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public Vector3 gravitationalForce(Body b) {
        Vector3 direction = b.massCenter.minus(this.massCenter);
        double distance = direction.length();
        direction.normalize();
        double force = Simulation.G * this.mass * b.mass / (distance * distance);
        return direction.times(force);
    }

    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force.)
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public void move(Vector3 force) {
        Vector3 newPos = force.times(1 / mass).plus(massCenter).plus(currentMovement);
        Vector3 newMov = newPos.minus(massCenter);

        massCenter = newPos;
        currentMovement = newMov;
    }

    // Returns the approximate radius of this body.
    // (It is assumed that the radius r is related to the mass m of the body by r = m ^ 0.5,
    // where m and r measured in solar units.)
    public double radius() {
        // Duplicate code is not good
        return SpaceDraw.massToRadius(mass);
    }

    // Returns a new body that is formed by the collision of this body and 'b'. The impulse
    // of the returned body is the sum of the impulses of 'this' and 'b'.
    public Body merge(Body b) {
        return new Body(
                this.mass + b.mass,
                calculateWeighted(this.massCenter, this.mass, b.massCenter, b.mass),
                calculateWeighted(this.currentMovement, this.mass, b.currentMovement, b.mass)
        );
    }
    
    public void mergeInPlace(Body b) {
        this.massCenter = calculateWeighted(this.massCenter, this.mass, b.massCenter, b.mass);
        this.currentMovement = calculateWeighted(this.currentMovement, this.mass, b.currentMovement, b.mass);
        this.mass = this.mass + b.mass;
    }
    
    private Vector3 calculateWeighted(Vector3 vectorA, double massA, Vector3 vectorB, double massB) {
        return vectorA.times(massA).plus(vectorB.times(massB)).times(1 / (massA + massB));
    }

    // Draws the body to the specified canvas as a filled circle.
    // The radius of the circle corresponds to the radius of the body
    // (use a conversion of the real scale to the scale of the canvas as
    // in 'Simulation.java').
    // Hint: call the method 'drawAsFilledCircle' implemented in 'Vector3'.
    public void draw(CodeDraw cd) {
        cd.setColor(SpaceDraw.massToColor(mass));
        this.massCenter.drawAsFilledCircle(cd, radius());
    }

    public boolean isCollidingWith(Body body) {
        return this.distanceTo(body) < (this.radius() + body.radius());
    }

    // Switches the position of this and the given body
    public void switchPositions(Body body) {
        Vector3 tmpPos = this.massCenter;
        this.massCenter = body.massCenter;
        body.massCenter = tmpPos;
    }

    // Returns a string with the information about this body including
    // mass, position (mass center) and current movement. Example:
    // "5.972E24 kg, position: [1.48E11,0.0,0.0] m, movement: [0.0,29290.0,0.0] m/s."
    public String toString() {
        return String.format("%f kg, position: %s m, movement: %s m/s", mass, massCenter, currentMovement);
    }

    public double mass() {
        return mass;
    }

    public Vector3 massCenter() {
        return massCenter;
    }

    @Override
    public Massive copy() {
        return new Body(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body body = (Body) o;
        return Double.compare(body.mass, mass) == 0 && Objects.equals(massCenter, body.massCenter) && Objects.equals(currentMovement, body.currentMovement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mass, massCenter, currentMovement);
    }
}

