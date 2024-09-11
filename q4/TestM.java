import org.junit.*;
import static org.junit.Assert.*;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public class TestM {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    // Test set 1: Node coverage but not edge coverage
    @Test
    public void testNodeCoverage() {
        // Covers nodes: 1, 2, 3, 4, 8, 10, 11
        M obj = new M();
        obj.m("", 0);
        assertEquals("zero\n", outContent.toString());
    }

    // Test set 2: Edge coverage but not edge-pair coverage
    @Test
    public void testEdgeCoverage() {
        // Covers edges: 1->2, 2->3, 3->4, 3->5, 3->6, 3->7, 4->8, 8->10, 10->11
        M obj = new M();
        obj.m("", 0);
        obj.m("a", 1);
        obj.m("aa", 2);
        obj.m("aaa", 3);
    }

    // Test set 3: Edge-pair coverage but not prime path coverage
    @Test
    public void testEdgePairCoverage() {
        // Covers edge pairs: 1->2->3, 2->3->4, 3->4->8, 4->8->10, 8->10->11, etc.
        M obj = new M();
        obj.m("", 0);
        obj.m("a", 1);
        obj.m("aa", 2);
        obj.m("aaa", 3);
    }

    // Test set 4: Prime path coverage
    @Test
    public void testPrimePathCoverage() {
        // Covers all prime paths in the CFG.
        M obj = new M();
        obj.m("", 0);
        obj.m("a", 1);
        obj.m("aa", 2);
        obj.m("aaa", 3);
    }

    class M {
        public static void main(String[] argv) {
            M obj = new M();
            if (argv.length > 0)
                obj.m(argv[0], argv.length);
        }

        public void m(String arg, int i) {
            int q = 1;
            A o = null;
            Impossible nothing = new Impossible();
            if (i == 0)
                q = 4;
            q++;
            switch (arg.length()) {
                case 0: q /= 2; break;
                case 1: o = new A(); new B(); q = 25; break;
                case 2: o = new A(); q = q * 100;
                default: o = new B(); break;
            }
            if (arg.length() > 0) {
                o.m();
            } else {
                System.out.println("zero");
            }
            nothing.happened();
        }
    }

    class A {
        public void m() {
            System.out.println("a");
        }
    }

    class B extends A {
        public void m() {
            System.out.println("b");
        }
    }

    class Impossible {
        public void happened() {
            // "2b||!2b?", whatever the answer nothing happens here
        }
    }
}