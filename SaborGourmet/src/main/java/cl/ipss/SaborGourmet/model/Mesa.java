package cl.ipss.SaborGourmet.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Mesa {
    @Positive (message = "El numero de la mesa debe ser igual o mayor a 1")
    private int numeroMesa;
    @Min (value=1, message = "La cantidad minima de comensales debe ser al menos 1")
    private int cantidadComensal;
    @NotBlank (message = "El sector es obligatorio, TERRAZA o SALON")
    private String sector;
    private boolean disponible;

    public Mesa() {
    }

    public Mesa(int numeroMesa, int cantidadComensal, String sector, boolean disponible) {
        this.numeroMesa = numeroMesa;
        this.cantidadComensal = cantidadComensal;
        this.sector = sector;
        this.disponible = disponible;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getCantidadComensal() {
        return cantidadComensal;
    }

    public void setCantidadComensal(int numeroComensal) {
        this.cantidadComensal = numeroComensal;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    
    
}
