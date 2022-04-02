import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BodyLinkedListTest {
    BodyLinkedList bll;

    private static final Body sun = new Body(1.989e30, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
    private static final Body earth = new Body(5.972e24, new Vector3(-1.394555e11, 5.103346e10, 0), new Vector3(-10308.53, -28169.38, 0));
    
    @BeforeEach
    void setUp() {
        bll = new BodyLinkedList();
        bll.addLast(sun);
        bll.addLast(earth);
    }

    @Test
    void removeCollidingWith() {
        BodyLinkedList removed = bll.removeCollidingWith(sun);
        assertEquals(1, removed.size());
        assertEquals(sun, removed.pollFirst());
        assertEquals(1, bll.size());
        assertEquals(earth, bll.pollFirst());
    }
}