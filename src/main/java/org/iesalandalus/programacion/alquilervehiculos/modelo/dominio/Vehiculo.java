package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

public abstract class Vehiculo {
    private static final String ER_MARCA = "^[A-Za-z]{3,20}[\\-|\\s+]?([A-Za-z]{3,20})?$";
    private static final String ER_MATRICULA = "^[0-9]{4}[B-Z]{3}$";
    private String marca;
    private String modelo;
    private String matricula;

    // Constructor con parámetros
    protected Vehiculo(String marca, String modelo, String matricula) {
        matricula = matricula.toUpperCase();
        setMarca(marca);
        setModelo(modelo);
        setMatricula(matricula);
    }

    // Constructor copia
    protected Vehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No es posible copiar un vehiculo nulo.");
        }
        setMarca(vehiculo.getMarca());
        setModelo(vehiculo.getModelo());
        setMatricula(vehiculo.getMatricula());
    }

    public static Vehiculo copiar(Vehiculo vehiculo) {

        if (vehiculo instanceof Turismo) {
            vehiculo = new Turismo((Turismo) vehiculo);
        }
        if (vehiculo instanceof Autobus) {
            vehiculo = new Autobus((Autobus) vehiculo);
        }
        if (vehiculo instanceof Furgoneta) {
            vehiculo = new Furgoneta((Furgoneta) vehiculo);
        }
        return vehiculo;
    }

    protected abstract int getFactorPrecio();

    // Método devuelve vehiculo con matricula indicada
    public static Vehiculo getVehiculoConMatricula(String matricula) {
        return new Turismo("Ford", "Mondeo", matricula, 3000); // Devolver vehiculo con matricula obtenida
    }

    // Get y Set de marca
    public String getMarca() {
        return marca;
    }

    protected void setMarca(String marca) {
        if (marca == null) {
            throw new NullPointerException("ERROR: La marca no puede ser nula.");
        }
        if (!marca.matches(ER_MARCA)) {
            throw new IllegalArgumentException("ERROR: La marca no tiene un formato válido.");
        }
        this.marca = marca;
    }

    // Get y Set de modelo
    public String getModelo() {
        return modelo;
    }

    protected void setModelo(String modelo) {
        if (modelo == null) { // Validar modelo no nulo
            throw new NullPointerException("ERROR: El modelo no puede ser nulo.");
        }
        if (modelo.trim().isEmpty()) { // Validar modelo no esta blanco
            throw new IllegalArgumentException("ERROR: El modelo no puede estar en blanco.");
        }
        this.modelo = modelo;
    }

    // Get y Set de matricula
    public String getMatricula() {
        return matricula;
    }

    protected void setMatricula(String matricula) {
        if (matricula == null) { // Validar matricula no nula
            throw new NullPointerException("ERROR: La matrícula no puede ser nula.");
        }
        if (!matricula.matches(ER_MATRICULA)) { // Validar formato de la matricula
            throw new IllegalArgumentException("ERROR: La matrícula no tiene un formato válido.");
        }
        this.matricula = matricula;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Vehiculo))
            return false;
        Vehiculo other = (Vehiculo) obj;
        if (matricula == null) {
            if (other.matricula != null)
                return false;
        } else if (!matricula.equals(other.matricula))
            return false;
        return true;
    }

}
