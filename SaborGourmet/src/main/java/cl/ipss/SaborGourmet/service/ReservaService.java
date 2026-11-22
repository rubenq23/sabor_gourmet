package cl.ipss.SaborGourmet.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import cl.ipss.SaborGourmet.model.Mesa;
import cl.ipss.SaborGourmet.model.Reserva;
import cl.ipss.SaborGourmet.repository.ClienteRepository;
import cl.ipss.SaborGourmet.repository.MesaRepository;
import cl.ipss.SaborGourmet.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;

    // Inyeccion de dependencias por constructor
    public ReservaService(
            ReservaRepository reservaRepository,
            MesaRepository mesaRepository,
            ClienteRepository clienteRepository) {

        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
        this.clienteRepository = clienteRepository;
    }

    // Obtener todas las reservas
    public List<Reserva> obtenerTodos() {
        return reservaRepository.findAll();
    }

    // Buscar reserva por id
    public Reserva buscarIdReserva(int id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Reserva con ID " + id + " no encontrada"));
    }

    // Verificar si una mesa ya esta reservada
    private boolean mesaOcupada(
            int numeroMesa,
            LocalDate dia,
            LocalTime hora,
            int idExcluir) {

        return reservaRepository.findAll().stream()
                .anyMatch(r ->
                        r.getNumeroMesa() == numeroMesa
                        && r.getDia().equals(dia)
                        && r.getHora().equals(hora)
                        && "CONFIRMADA".equals(r.getEstado())
                        && r.getIdReserva() != idExcluir);
    }

    // Validar los datos y relaciones de la reserva
    private void validarReserva(Reserva reserva, int idExcluir) {

        // Verificar que exista el cliente
        if (clienteRepository.findbyId(
                reserva.getClienteId()).isEmpty()) {

            throw new IllegalArgumentException(
                    "El cliente no existe");
        }

        // Buscar mesa
        Mesa mesa = mesaRepository.findByNumeroMesa(
                reserva.getNumeroMesa())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La mesa no existe"));

        // Verificar disponibilidad general
        if (!mesa.isDisponible()) {
            throw new IllegalStateException(
                    "La mesa no esta habilitada");
        }

        // Verificar capacidad
        if (reserva.getCantidadComensal()
                > mesa.getCantidadComensal()) {

            throw new IllegalArgumentException(
                    "La cantidad de comensales supera la capacidad");
        }

        // Verificar sector
        if (!reserva.getSector().equals(mesa.getSector())) {
            throw new IllegalArgumentException(
                    "El sector no corresponde a la mesa");
        }

        // Verificar reserva existente
        if (mesaOcupada(
                reserva.getNumeroMesa(),
                reserva.getDia(),
                reserva.getHora(),
                idExcluir)) {

            throw new IllegalStateException(
                    "La mesa ya esta reservada para ese dia y hora");
        }
    }

    // Crear reserva
    public synchronized Reserva crearReserva(Reserva reserva) {

        validarReserva(reserva, 0);

        reserva.setIdReserva(null);
        reserva.setEstado("CONFIRMADA");

        return reservaRepository.save(reserva);
    }

    // Actualizar reserva
    public synchronized Reserva actualizarReserva(
            int id, Reserva datosActualizados) {

        Reserva existente = buscarIdReserva(id);

        if ("CANCELADA".equals(existente.getEstado())) {
            throw new IllegalStateException(
                    "No se puede modificar una reserva cancelada");
        }

        validarReserva(datosActualizados, id);

        existente.setDia(datosActualizados.getDia());
        existente.setHora(datosActualizados.getHora());
        existente.setSector(datosActualizados.getSector());
        existente.setNumeroMesa(datosActualizados.getNumeroMesa());
        existente.setCantidadComensal(
                datosActualizados.getCantidadComensal());
        existente.setClienteId(datosActualizados.getClienteId());

        return reservaRepository.save(existente);
    }

    // Cancelar reserva sin eliminarla del JSON
    public synchronized Reserva cancelarReserva(int id) {

        Reserva reserva = buscarIdReserva(id);

        if ("CANCELADA".equals(reserva.getEstado())) {
            throw new IllegalStateException(
                    "La reserva ya esta cancelada");
        }

        reserva.setEstado("CANCELADA");

        return reservaRepository.save(reserva);
    }

    // Consultar mesas disponibles
    public List<Mesa> consultarDisponibles(
            LocalDate dia,
            LocalTime hora,
            int personas) {

        if (personas < 1) {
            throw new IllegalArgumentException(
                    "La cantidad de personas debe ser mayor a 0");
        }

        return mesaRepository.findAll().stream()
                .filter(Mesa::isDisponible)
                .filter(m -> m.getCantidadComensal() >= personas)
                .filter(m -> !mesaOcupada(
                        m.getNumeroMesa(), dia, hora, 0))
                .toList();
    }
}