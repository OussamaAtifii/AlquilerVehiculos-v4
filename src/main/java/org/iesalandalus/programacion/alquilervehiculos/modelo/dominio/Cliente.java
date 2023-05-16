package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

public class Cliente {
    private static final String ER_NOMBRE = "^([A-Z]([a-z])+){1}[\\s]?([A-Z]([a-z])+)*$";
    private static final String ER_DNI = "^[0-9]{8}[a-zA-Z]$";
    private static final String ER_TELEFONO = "^[6-9][0-9]{8}$";
    private String nombre;
    private String dni;
    private String telefono;

    // Constructor con parámetros
    public Cliente(String nombre, String dni, String telefono) {
        setNombre(nombre);
        setDni(dni);
        setTelefono(telefono);
    }

    // Constructor copia
    public Cliente(Cliente cliente) {
        if (cliente == null) { // Validar cliente no nulo
            throw new NullPointerException("ERROR: No es posible copiar un cliente nulo.");
        }

        setNombre(cliente.getNombre());
        setDni(cliente.getDni());
        setTelefono(cliente.getTelefono());
    }

    // Método devuelve cliente con el dni indicado
    public static Cliente getClienteConDni(String dni) {
        return new Cliente("Vladimir Putin", dni, "666666666"); // Devolver cliente con dni obtenido
    }

    // Get y Set de nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null) { // Validar nombre no nulo
            throw new NullPointerException("ERROR: El nombre no puede ser nulo.");
        }
        // if (!nombre.matches(ER_NOMBRE)) { // Validar formato nombre
        // throw new IllegalArgumentException("ERROR: El nombre no tiene un formato
        // válido.");
        // }
        this.nombre = nombre;
    }

    // Get y Set de dni
    public String getDni() {
        return dni;
    }

    private void setDni(String dni) {
        if (dni == null) { // Validar dni no nulo
            throw new NullPointerException("ERROR: El DNI no puede ser nulo.");
        }
        if (!dni.matches(ER_DNI)) { // Validar formato dni
            throw new IllegalArgumentException("ERROR: El DNI no tiene un formato válido.");
        }

        if (!comprobarLetraDni(dni)) { // Validamos letra dni correcta
            throw new IllegalArgumentException("ERROR: La letra del DNI no es correcta.");
        }

        this.dni = dni.toUpperCase(); // Asignamos y ponemos letra mayúscula
    }

    private boolean comprobarLetraDni(String dni) {
        String letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letra = Character.toUpperCase(dni.charAt(8));
        // Validar letra dni correcta.
        return letra == letrasValidas.charAt(Integer.parseInt(dni.substring(0, 8)) % 23);
    }

    // Get y Set de telefono
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null) {
            throw new NullPointerException("ERROR: El teléfono no puede ser nulo.");
        }
        if (!telefono.matches(ER_TELEFONO)) {
            throw new IllegalArgumentException("ERROR: El teléfono no tiene un formato válido.");
        }
        this.telefono = telefono;
    }

    // Método toString
    @Override
    public String toString() {
        return nombre + " - " + dni + " (" + telefono + ")";
    }

    // Método hashCode
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dni == null) ? 0 : dni.hashCode());
        return result;
    }

    /*
     * Método equals, un cliente es igual a otro si su DNI es el mismo
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cliente other = (Cliente) obj;
        if (dni == null) {
            if (other.dni != null)
                return false;
        } else if (!dni.equals(other.dni))
            return false;
        return true;
    }

}
