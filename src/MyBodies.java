import java.util.Random;

public class MyBodies {

    static final Body sun = new Body(1.989e30,new Vector3(0,0,0),new Vector3(0,0,0));
    static final  Body earth = new Body(5.972e24,new Vector3(-1.394555e11,5.103346e10,0),new Vector3(-10308.53,-28169.38,0));
    static final Body mercury = new Body(3.301e23,new Vector3(-5.439054e10,9.394878e9,0),new Vector3(-17117.83,-46297.48,-1925.57));
    static final Body venus = new Body(4.86747e24,new Vector3(-1.707667e10,1.066132e11,2.450232e9),new Vector3(-34446.02,-5567.47,2181.10));
    static final Body mars = new Body(6.41712e23,new Vector3(-1.010178e11,-2.043939e11,-1.591727E9),new Vector3(20651.98,-10186.67,-2302.79));

    static final Body[] solBodies = {sun, earth, mercury, venus, mars};
    
    static Body[] getBodies(String instance) {
        if(instance.equalsIgnoreCase("sol")) {
            return solBodies;
        }
        
        Body[] ret = new Body[Simulation.NUMBER_OF_BODIES];

        Random random = new Random(2022);

        for (int i = 0; i < Simulation.NUMBER_OF_BODIES; i++) {
            Body b = new Body(
                    Math.abs(random.nextGaussian()) * Simulation.OVERALL_SYSTEM_MASS / Simulation.NUMBER_OF_BODIES,
                    new Vector3(0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU, 0.2 * random.nextGaussian() * Simulation.AU),
                    new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3)
            );
            ret[i] = b;
        }
        
        return ret;
    }

}
