///[SinErrores]
class Target {
    int finalValue; // The int attribute we want to assign to
}

class Intermediate {
    Target t;
    public Intermediate() {
        this.t = new Target();
        this.t.finalValue = 123; // Give it an initial value
    }
    Target getTarget() {
        return t;
    }
}

class Source {
    Intermediate inter;
    public Source() {
        this.inter = new Intermediate();
    }
    Intermediate getIntermediate() {
        return inter;
    }
}

class Principal {
    static void main() {
        var s = new Source();
        var targetInstance = new Target(); // Instance where we'll store the result

        targetInstance.finalValue = (s.getIntermediate()).getTarget().finalValue;

        System.printIln(targetInstance.finalValue); // Should print 123
    }
}