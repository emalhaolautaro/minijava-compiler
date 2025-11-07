///[SinErrores]
class Producto {
    int id;
    public Producto(int i) {
        this.id = i;
    }
    int getId() {
        return id;
    }
}

class Almacen {
    Producto p;
    public Almacen(int i) {
        this.p = new Producto(i);
    }
    Producto getProducto() {
        return p;
    }
}

class Fabrica {
    Almacen a;

    public Fabrica() {
        this.a = new Almacen(123);
    }

    Almacen getAlmacen() {
        return a;
    }
}

class Sucursal extends Fabrica {
    public Sucursal(int i) {

    }

    Fabrica getEstaFabrica() {
        return this;
    }
}

class Principal {
    static Sucursal getSucursalGlobal() {
        return new Sucursal(999);
    }

    static void main() {
        var idFinal = Principal.getSucursalGlobal().getEstaFabrica().getAlmacen().getProducto().getId();

        System.printIln(idFinal);
    }
}