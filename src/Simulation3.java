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
        if (args.length > 0 && args[0].equalsIgnoreCase("sol")) {
            bodies.addLast(new Body(1.989e30, new Vector3(0, 0, 0), new Vector3(0, 0, 0)));
            bodies.addLast(new Body(5.972e24, new Vector3(-1.394555e11, 5.103346e10, 0), new Vector3(-10308.53, -28169.38, 0)));
            bodies.addLast(new Body(3.301e23, new Vector3(-5.439054e10, 9.394878e9, 0), new Vector3(-17117.83, -46297.48, -1925.57)));
            bodies.addLast(new Body(4.86747e24, new Vector3(-1.707667e10, 1.066132e11, 2.450232e9), new Vector3(-34446.02, -5567.47, 2181.10)));
            bodies.addLast(new Body(6.41712e23, new Vector3(-1.010178e11, -2.043939e11, -1.591727E9), new Vector3(20651.98, -10186.67, -2302.79)));
            for (Body b : bodies) {
                forceOnBody.put(b, new Vector3());
            }
        } else {
            Random random = new Random(2022);

            for (int i = 0; i < Simulation.NUMBER_OF_BODIES; i++) {
                Body b = new Body(
                        Math.abs(random.nextGaussian()) * Simulation.OVERALL_SYSTEM_MASS / Simulation.NUMBER_OF_BODIES,
                        new Vector3(0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU),
                        new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3)
                );
                forceOnBody.put(b, new Vector3());
                bodies.addLast(b);
            }
        }

        double seconds = 0;

        while (true) {
            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // merge bodies that have collided
            for (Body b : bodies) {
                BodyLinkedList removed = bodies.removeCollidingWith(b);
                for (Body r : removed) {
                    b.merge(r);
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
