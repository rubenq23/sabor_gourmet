package cl.ipss.SaborGourmet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cl.ipss.SaborGourmet.model.Reserva;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
public class ReservaRepository {

    private final String archivo;
    private final ObjectMapper objectMapper;

    // Inyeccion de dependencia por constructor
    public ReservaRepository(
            @Value("${app.archivo.reserva}") String archivo,
            ObjectMapper objectMapper) {

        this.archivo = archivo;
        this.objectMapper = objectMapper;
    }

    // Metodo para obtener todas las reservas
    public synchronized List<Reserva> findAll() {

        File file = new File(archivo);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                file, new TypeReference<List<Reserva>>() {});
    }

    // Metodo privado para guardar todas las reservas
    private void saveAll(List<Reserva> reservas) {

        File file = new File(archivo);

        File directorio = file.getParentFile();

        if (directorio != null) {
            directorio.mkdirs();
        }

        objectMapper.writeValue(file, reservas);
    }

    // Metodo para crear o actualizar una reserva
    public synchronized Reserva save(Reserva reserva) {

        List<Reserva> reservas = findAll();

        if (reserva.getIdReserva() == null || reserva.getIdReserva() == 0) {
            // Genera un id autoincremental
            int nextId = reservas.stream()
                    .mapToInt(Reserva::getIdReserva)
                    .max()
                    .orElse(0) + 1;

            reserva.setIdReserva(nextId);
            reservas.add(reserva);

        } else {

            // Busca y reemplaza la reserva existente
            boolean encontrada = false;

            for (int i = 0; i < reservas.size(); i++) {

                if (reservas.get(i).getIdReserva()
                        == reserva.getIdReserva()) {

                    reservas.set(i, reserva);
                    encontrada = true;
                    break;
                }
            }

            if (!encontrada) {
                throw new IllegalArgumentException(
                        "La reserva no existe");
            }
        }

        saveAll(reservas);
        return reserva;
    }

    // Metodo para buscar reserva por id
    public synchronized Optional<Reserva> findById(int id) {

        return findAll().stream()
                .filter(r -> r.getIdReserva() == id)
                .findFirst();
    }
}