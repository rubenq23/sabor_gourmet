package cl.ipss.SaborGourmet.model;


import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Reserva {
    private int idReserva;
    @NotNull  (message = "dia de la reserva es obligatorio")
    private LocalDate dia;
    @NotNull (message = "La hora de la reserva es obligatoria")
    private LocalTime hora;
    @NotBlank (message = "El sector es obligatorio, TERRAZA o SALON")
    private String sector;
    private int numeroMesa;
    @Min (value=1, message = "La cantidad minima de comensales debe ser al menos 1")
    private int cantidadComensal;
    @Min (value= 1, message = "El id del cliente es obligatorio")
    private int clienteId;
    
    
    public Reserva() {
    }

    public Reserva(int idReserva, LocalDate dia, LocalTime hora, String sector, int numeroMesa, int cantidadComensal,
            int clienteId) {
        this.idReserva = idReserva;
        this.dia = dia;
        this.hora = hora;
        this.sector = sector;
        this.numeroMesa = numeroMesa;
        this.cantidadComensal = cantidadComensal;
        this.clienteId = clienteId;
       
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public void setCantidadComensal(int cantidadComensal) {
        this.cantidadComensal = cantidadComensal;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

}
