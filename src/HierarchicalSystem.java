import codedraw.CodeDraw;

// A cosmic system that is composed of a central named body (of type 'NamedBodyForcePair')
// and an arbitrary number of subsystems (of type 'HierarchicalSystem') in its orbit.
// This class implements 'CosmicSystem'.
//
public class HierarchicalSystem implements CosmicSystem {
    private NamedBodyForcePair central;
    private CosmicSystem[] inOrbit;
    
    // Initializes this system with a name and a central body.
    public HierarchicalSystem(NamedBodyForcePair central, CosmicSystem... inOrbit) {
        this.central = central;
        this.inOrbit = inOrbit;
    }

    @Override
    public Vector3 getMassCenter() {
        Vector3 massCenter = new Vector3();
        for(Body body : getBodies()) {
            massCenter = massCenter.plus(body.massCenter()).times(body.mass());
        }
        return massCenter.times(1 / getMass());
    }

    @Override
    public double getMass() {
        double mass = central.getMass();
        for(CosmicSystem cs : inOrbit) {
            mass += cs.getMass();
        }
        return mass;
    }

    @Override
    public int numberOfBodies() {
        int num = 1;
        for(CosmicSystem cs : inOrbit) {
            num += cs.numberOfBodies();
        }
        return num;
    }

    @Override
    public double distanceTo(CosmicSystem cs) {
        return this.central.getMassCenter().distanceTo(cs.getMassCenter());
    }

    @Override
    public void addForceFrom(Body b) {
        this.central.addForceFrom(b);
        for(CosmicSystem cs : inOrbit) {
            cs.addForceFrom(b);
        }
    }

    @Override
    public void addForceTo(CosmicSystem cs) {
        this.central.addForceTo(cs);
        for(CosmicSystem _cs : inOrbit) {
            _cs.addForceTo(cs);
        }
    }

    @Override
    public BodyLinkedList getBodies() {
        BodyLinkedList list = new BodyLinkedList();
        list.addAllLast(this.central.getBodies());
        for(CosmicSystem cs : inOrbit) {
            list.addAllLast(cs.getBodies());
        }
        return list;
    }

    @Override
    public void update() {
        this.central.update();
        for(CosmicSystem cs : inOrbit) {
            cs.update();
        }
    }

    @Override
    public boolean collidesWith(NamedBodyForcePair b) {
        for(Body body : b.getBodies()) {
            for(Body other : this.getBodies()) {
                if(body.isCollidingWith(other)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void draw(CodeDraw cd) {
        this.central.draw(cd);
        for(CosmicSystem cs : inOrbit) {
            cs.draw(cd);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.central.toString());
        sb.append(" {");
        for(CosmicSystem cs : inOrbit) {
            sb.append(cs.toString());
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }
}
