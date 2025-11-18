///[SinErrores]

class Node {
    int val;
    Node next;
}

class List {
    Node head;

    void addLast(int x) {
        var n = new Node();
        n.val = x;

        if (head == null) {
            head = n;
        } else {
            var p = head;
            while (p.next != null) {
                p = p.next;
            }
            p.next = n;
        }
    }

    void printAll() {
        var p = head;
        while (p != null) {
            debugPrint(p.val);
            p = p.next;
        }
    }
}

class FancyList extends List {

    // Redefinición SIN super: si agregás 2 → lo duplica
    void addLast(int x) {

        // --- Copia del addLast original ---
        var n = new Node();
        n.val = x;

        if (head == null) {
            head = n;
        } else {
            var p = head;
            while (p.next != null) {
                p = p.next;
            }
            p.next = n;
        }

        // --- Comportamiento adicional solo en FancyList ---
        if (x == 2) {
            var n2 = new Node();
            n2.val = x;

            var q = head;
            while (q.next != null) {
                q = q.next;
            }
            q.next = n2;
        }
    }
}

class Init {
    static void main() {
        var l = new FancyList();

        l.addLast(1);   // → [1]
        l.addLast(2);   // → [1, 2, 2]

        l.printAll();   // imprime 1, 2, 2
    }
}