package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.naming.OperationNotSupportedException;

public class Alquiler {
    public final static DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final static int PRECIO_DIA = 20;
    private LocalDate fechaAlquiler;
    private LocalDate fechaDevolucion;
    private Vehiculo vehiculo;
    private Cliente cliente;

    // Constructor con parámetros
    public Alquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler) {
        setCliente(cliente);
        setVehiculo(vehiculo);
        setFechaAlquiler(fechaAlquiler);
    }

    // Constructor copia
    public Alquiler(Alquiler alquiler) {
        if (alquiler == null) { // Validar alquiler no nulo
            throw new NullPointerException("ERROR: No es posible copiar un alquiler nulo.");
        }
        if (alquiler.getFechaDevolucion() == null) {
            throw new NullPointerException("ERROR: La fecha de devolución no puede ser nula.");
        }

        if (alquiler.getVehiculo() instanceof Turismo) {
            setVehiculo(new Turismo((Turismo) alquiler.getVehiculo()));
        }
        if (alquiler.getVehiculo() instanceof Furgoneta) {
            setVehiculo(new Furgoneta((Furgoneta) alquiler.getVehiculo()));
        }
        if (alquiler.getVehiculo() instanceof Autobus) {
            setVehiculo(new Autobus((Autobus) alquiler.getVehiculo()));
        }

        setCliente(new Cliente(alquiler.getCliente()));
        setFechaAlquiler(alquiler.getFechaAlquiler());
        setFechaDevolucion(alquiler.getFechaDevolucion());
    }

    // Método asigna fecha de devolución si es correcta
    public void devolver(LocalDate fechaDevolucion) throws OperationNotSupportedException {
        if (fechaDevolucion == null) { // Validar fecha no nula
            throw new NullPointerException("ERROR: La fecha de devolución no puede ser nula.");
        }
        if (this.fechaDevolucion != null) { // Validar que alquiler aun no esta devuelto
            throw new OperationNotSupportedException("ERROR: La devolución ya esta registrada.");
        }
        setFechaDevolucion(fechaDevolucion);
    }

    // Método devuelve precio
    public int getPrecio() {
        if (fechaDevolucion == null) { // Si no hay fecha de devolución, aun no hay precio
            return 0;
        } else {
            int numDias = fechaDevolucion.getDayOfMonth() - fechaAlquiler.getDayOfMonth();
            return (PRECIO_DIA + vehiculo.getFactorPrecio()) * numDias;
        }
    }

    // Getter y setter fecha de alquiler
    public LocalDate getFechaAlquiler() {
        return fechaAlquiler;
    }

    private void setFechaAlquiler(LocalDate fechaAlquiler) {
        if (fechaAlquiler == null) { // Validar fecha no nula
            throw new NullPointerException("ERROR: La fecha de alquiler no puede ser nula.");
        }
        if (fechaAlquiler.isAfter(LocalDate.now())) { // Validar fecha alquiler no supera el dia de hoy
            throw new IllegalArgumentException("ERROR: La fecha de alquiler no puede ser futura.");
        }
        this.fechaAlquiler = fechaAlquiler;
    }

    // Getter y setter fecha de devolución
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    private void setFechaDevolucion(LocalDate fechaDevolucion) {
        /*
         * Validar fecha devolución no igual a fecha alquiler Validar fecha devolución
         * no es anterior a fecha alquiler
         */
        if (fechaDevolucion != null) {
            if (fechaDevolucion.equals(fechaAlquiler) ||
                    fechaDevolucion.isBefore(fechaAlquiler)) {
                throw new IllegalArgumentException(
                        "ERROR: La fecha de devolución debe ser posterior a la fecha de alquiler.");
            }
            if (fechaDevolucion.isAfter(LocalDate.now())) { // Validar fecha alquiler no posterior hoy
                throw new IllegalArgumentException("ERROR: La fecha de devolución no puede ser futura.");
            }
        }

        this.fechaDevolucion = fechaDevolucion;
    }

    // Getter y setter de vehiculo
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    private void setVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) { // Validar vehiculo no nulo
            throw new NullPointerException("ERROR: El vehiculo no puede ser nulo.");
        }
        this.vehiculo = vehiculo;
    }

    // Getter y setter de cliente
    public Cliente getCliente() {
        return cliente;
    }

    private void setCliente(Cliente cliente) {
        if (cliente == null) { // Validar cliente no nulo
            throw new NullPointerException("ERROR: El cliente no puede ser nulo.");
        }
        this.cliente = cliente;
    }

    // Método hashCode
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fechaAlquiler == null) ? 0 : fechaAlquiler.hashCode());
        result = prime * result + ((vehiculo == null) ? 0 : vehiculo.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        return result;
    }

    /*
     * Método equals, un alquiler es igual a otro cuando cliente, vehiculo y fecha
     * alquiler son lo mismo
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alquiler other = (Alquiler) obj;
        if (fechaAlquiler == null) {
            if (other.fechaAlquiler != null)
                return false;
        } else if (!fechaAlquiler.equals(other.fechaAlquiler))
            return false;
        if (vehiculo == null) {
            if (other.vehiculo != null)
                return false;
        } else if (!vehiculo.equals(other.vehiculo))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        return true;
    }

    // Método toString
    @Override
    public String toString() {
        if (fechaDevolucion != null) { // Cuando no hay fecha de devolución añadiremos "Aún no devuelto"
            return cliente + " <---> " + vehiculo + ", " + fechaAlquiler.format(FORMATO_FECHA) + " - "
                    + fechaDevolucion.format(FORMATO_FECHA) + " (" + getPrecio() + "€)";
        } else { // En caso contrario añadiremos la fecha de devolución.
            return cliente + " <---> " + vehiculo + ", " + fechaAlquiler.format(FORMATO_FECHA) + " - "
                    + "Aún no devuelto (" + getPrecio() + "€)";
        }

    }

}
