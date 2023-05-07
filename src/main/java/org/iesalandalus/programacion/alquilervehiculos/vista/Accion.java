package org.iesalandalus.programacion.alquilervehiculos.vista;

public enum Accion {
    INSERTAR_CLIENTE("Insertar cliente"),
    INSERTAR_VEHICULO("Insertar vehiculo"),
    INSERTAR_ALQUILER("Insertar alquiler"),
    BUSCAR_CLIENTE("Buscar cliente"),
    BUSCAR_VEHICULO("Buscar vehiculo"),
    BUSCAR_ALQUILER("Buscar alquiler"),
    MODIFICAR_CLIENTE("Modificar cliente"),
    BORRAR_CLIENTE("Borrar cliente"),
    BORRAR_VEHICULO("Borrar vehiculo"),
    BORRAR_ALQUILER("Borrar alquiler"),
    LISTAR_CLIENTES("Listar clientes"),
    LISTAR_VEHICULO("Listar vehiculos"),
    LISTAR_ALQUILERES("Listar alquileres"),
    LISTAR_ALQUILERES_CLIENTE("Listar alquileres de cliente"),
    LISTAR_ALQUILERES_VEHICULO("Listar alquileres de vehiculo"),
    DEVOLVER_ALQUILER_CLIENTE("Devolver alquiler de cliente"),
    DEVOLVER_ALQUILER_VEHICULO("Devolver alquiler de vehiculo"),
    MOSTRAR_ESTADISTICAS_MENSUALES("Mostrar estadisticas mensuales"),
    SALIR("Salir");

    String cadenaAMostrar;

    private Accion(String texto) {
        this.cadenaAMostrar = texto;
    }

    private static boolean esOrdinalValido(int ordinal) {
        Accion[] opciones = values();
        return ordinal < 0 || ordinal > opciones.length;
    }

    public static Accion get(int ordinal) {
        if (esOrdinalValido(ordinal)) {
            throw new IllegalArgumentException("El ordinal es invalido.");
        }

        Accion[] opciones = values();

        return opciones[ordinal];
    }

    @Override
    public String toString() {
        return cadenaAMostrar;
    }

}
