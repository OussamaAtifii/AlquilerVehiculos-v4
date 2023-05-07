package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

public class Autobus extends Vehiculo {
    private static final int FACTOR_PLAZAS = 2;
    private int plazas;

    public Autobus(String marca, String modelo, String matricula, int plazas) {
        super(marca, modelo, matricula);
        setPlazas(plazas);
    }

    public Autobus(Autobus autobus) {
        super(autobus);
        setPlazas(autobus.getPlazas());
    }

    public int getPlazas() {
        return plazas;
    }

    private void setPlazas(int plazas) {
        if (plazas <= 0 ) {
            throw new IllegalArgumentException("ERROR: Las plazas no pueden ser negativas.");
        }
        this.plazas = plazas;
    }

    protected int getFactorPrecio() {
        return plazas * FACTOR_PLAZAS;
    }

    @Override
    public String toString() {
        return "🚌 " + getMarca() + " " + getModelo() + " (" + plazas + " plazas) - " + getMatricula();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
