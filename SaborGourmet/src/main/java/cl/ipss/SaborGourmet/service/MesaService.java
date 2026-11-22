package cl.ipss.SaborGourmet.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import cl.ipss.SaborGourmet.model.Mesa;
import cl.ipss.SaborGourmet.repository.MesaRepository;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    // Inyeccion de dependencia por constructor
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    // Metodo para obtener todas las mesas
    public List<Mesa> obtenerTodos() {
        return mesaRepository.findAll();
    }

    // Metodo para buscar mesa por numero
    public Mesa buscarNumeroMesa(int numeroMesa) {
        return mesaRepository.findByNumeroMesa(numeroMesa)
                .orElseThrow(() -> new NoSuchElementException(
                        "Mesa numero " + numeroMesa + " no encontrada"));
    }

    // Metodo para crear una mesa
    public Mesa crearMesa(Mesa mesa) {

        // Verifica que no exista una mesa con el mismo numero
        if (mesaRepository.findByNumeroMesa(mesa.getNumeroMesa()).isPresent()) {
            throw new IllegalArgumentException(
                    "La mesa numero " + mesa.getNumeroMesa() + " ya existe");
        }

        // Guarda la nueva mesa
        return mesaRepository.save(mesa);
    }

    // Metodo para actualizar una mesa
    public Mesa actualizarMesa(int numeroMesa, Mesa mesaActualizada) {

        // Verifica que la mesa exista
        Mesa mesaExistente = buscarNumeroMesa(numeroMesa);

        // Actualiza los datos de la mesa
        mesaExistente.setCantidadComensal(
                mesaActualizada.getCantidadComensal());

        mesaExistente.setSector(mesaActualizada.getSector());

        mesaExistente.setDisponible(mesaActualizada.isDisponible());

        // Guarda los datos actualizados
        return mesaRepository.save(mesaExistente);
    }

    // Metodo para eliminar una mesa
    public void eliminarMesa(int numeroMesa) {

        // Verifica que la mesa exista
        buscarNumeroMesa(numeroMesa);

        // Elimina la mesa del archivo JSON
        mesaRepository.deleteByNumeroMesa(numeroMesa);
    }
}