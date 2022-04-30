import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

// Simulates the formation of a massive solar system.
//
public class Simulation3 {

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();

        BodyLinkedList bodies = new BodyLinkedList();
        BodyForceTreeMap forceOnBody = new BodyForceTreeMap();

        // Call program with argument "sol" to run the solar system simulation
        Body[] injectBodies = MyBodies.getBodies(args.length > 0 ? args[0] : "");
        
        for(Body b : injectBodies) {
            bodies.addLast(b);
            forceOnBody.put(b, new Vector3());
        }

        double seconds = 0;

        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // merge bodies that have collided
            for (Body b : bodies) {
                BodyLinkedList removed = bodies.removeCollidingWith(b);
                for (Body r : removed) {
                    b.mergeInPlace(r);
                }
            }

            // for each body (with index i): compute the total force exerted on it.
            forceOnBody = new BodyForceTreeMap();
            for (Body b : bodies) {
                Vector3 force = new Vector3();
                for (Body g : bodies) {
                    if (b != g) {
                        force = force.plus(b.gravitationalForce(g));
                    }
                }
                forceOnBody.put(b, force);
            }

            // for each body (with index i): move it according to the total force exerted on it.
            for (Body b : bodies) {
                b.move(forceOnBody.get(b));
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                for (Body b : bodies) {
                    b.draw(cd);
                }

                // show new positions
                cd.show();
            }
        }
    }
}
