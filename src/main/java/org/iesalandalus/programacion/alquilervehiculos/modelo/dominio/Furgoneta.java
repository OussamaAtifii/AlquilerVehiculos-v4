package org.iesalandalus.programacion.alquilervehiculos.modelo.dominio;

public class Furgoneta extends Vehiculo {
    private static final int FACTOR_PLAZAS = 1;
    private static final int FACTOR_PMA = 100;
    private int plazas;
    private int pma;

    public Furgoneta(String marca, String modelo, int pma, int plazas, String matricula) {
        super(marca, modelo, matricula);
        setPlazas(plazas);
        setPma(pma);
    }

    public Furgoneta(Furgoneta furgoneta) {
        super(furgoneta);
        setPlazas(furgoneta.getPlazas());
        setPma(furgoneta.getPma());
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

    public int getPma() {
        return pma;
    }

    private void setPma(int pma) {
        if (pma <= 0 ) {
            throw new IllegalArgumentException("ERROR: El peso maximo autorizado no puede ser negativo.");
        }
        this.pma = pma;
    }

    protected int getFactorPrecio() {
        return pma / FACTOR_PMA + plazas * FACTOR_PLAZAS;
    }

    @Override
    public String toString() {
        return "? " + getMarca() + " " + getModelo() + " - Peso maximo autorizado: " + pma + " - (" + plazas + " plazas) - " + getMatricula();
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
