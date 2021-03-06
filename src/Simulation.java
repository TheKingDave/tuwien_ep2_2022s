import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

// 1. Grouping of methods and variables together to form units. Example: the new Body/Vector3
// 2. Hiding the internal data to keep data parity and stop unintended access to private data. This concept is used in 
//    the classes Body/Vector3 to hide the internal data (Body: mass, massCenter, currentMovement; Vector3: x, y, z).
//    This data can only be modified by the Object-methods.
// 3. The class or the object to call the method on. Object methods can be recognized by the absence of the 'static'
//    keyword in the class definition. It also can be recognized by the capitalization (as long as you stand by the 
//    java capitalization standards) of the first letter. Uppercase -> Class-method, lowercase -> Object-method

// Simulates the formation of a massive solar system.
public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // one light year
    public static final double LY = 9.461e15; // meters

    // some further constants needed in the simulation
    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters
    public static final double EARTH_MASS = 5.972e24; // kilograms
    public static final double EARTH_RADIUS = 6371e3; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 2 * AU; // the size of the square region in space
    public static final int NUMBER_OF_BODIES = 22;
    public static final double OVERALL_SYSTEM_MASS = 20 * SUN_MASS; // kilograms

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {
        // simulation
        CodeDraw cd = new CodeDraw();

        BodyQueue bodies = new BodyQueue(NUMBER_OF_BODIES);
        BodyForceMap forceOnBody = new BodyForceMap(NUMBER_OF_BODIES);

        Body[] injectBodies = MyBodies.getBodies(args.length > 0 ? args[0] : "");

        // Call program with argument "sol" to run the solar system simulation
        for(Body b : injectBodies) {
            bodies.add(b);
            forceOnBody.put(b, new Vector3());
        }

        double seconds = 0;

        // simulation loop
        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // for each body (with index i): compute the total force exerted on it.
            BodyQueue working = new BodyQueue(bodies);
            while (working.size() != 0) {
                Body b = working.poll();
                Vector3 force = new Vector3();
                for (int j = 0; j < bodies.size(); j++) {
                    Body g = bodies.poll();
                    if (b != g) {
                        force = force.plus(b.gravitationalForce(g));
                    }
                    bodies.add(g);
                }
                forceOnBody.put(b, force);
            }
            // now forceOnBody[i] holds the force vector exerted on body with index i.

            // for each body (with index i): move it according to the total force exerted on it.
            for (int i = 0; i < bodies.size(); i++) {
                Body b = bodies.poll();
                b.move(forceOnBody.get(b));
                bodies.add(b);
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                for (int i = 0; i < bodies.size(); i++) {
                    Body b = bodies.poll();
                    b.draw(cd);
                    bodies.add(b);
                }

                if (seconds % (2 * 24 * 60 * 60) == 0) {
                    Body tmpBody = null;
                    for (int i = 0; i < bodies.size(); i++) {
                        Body b = bodies.poll();
                        if (i == 2) {
                            tmpBody = b;
                        } else if (i == 4) {
                            assert tmpBody != null;
                            tmpBody.switchPositions(b);
                        }
                        bodies.add(b);
                    }
                }

                // show new positions
                cd.show();
            }
        }
    }
}
