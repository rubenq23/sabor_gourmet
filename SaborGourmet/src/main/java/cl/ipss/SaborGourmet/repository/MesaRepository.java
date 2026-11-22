package cl.ipss.SaborGourmet.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cl.ipss.SaborGourmet.model.Mesa;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
public class MesaRepository {

    private final String archivo;
    private final ObjectMapper objectMapper;

    // Constructor que recibe la ruta del archivo JSON
    public MesaRepository(@Value("${app.archivo.mesa}") String archivo) {
        this.archivo = archivo;
        this.objectMapper = new ObjectMapper();
    }

    // Metodo para obtener todas las mesas
    public synchronized List<Mesa> findAll() {

        File file = new File(archivo);

        // Si el archivo no existe, devuelve una lista vacia
        if (!file.exists()) {
            return new ArrayList<>();
        }

        // Lee el JSON y lo convierte en una lista de mesas
        return objectMapper.readValue(
                file, new TypeReference<List<Mesa>>() {});
    }

    // Metodo privado para guardar todas las mesas
    private void saveAll(List<Mesa> mesas) {

        File file = new File(archivo);

        // Crea el directorio si no existe
        File directorio = file.getParentFile();

        if (directorio != null) {
            directorio.mkdirs();
        }

        // Sobreescribe el archivo JSON
        objectMapper.writeValue(file, mesas);
    }

    // Metodo para crear o actualizar una mesa
    public synchronized Mesa save(Mesa mesa) {

        List<Mesa> mesas = findAll();
        boolean encontrada = false;

        // Busca si existe una mesa con el mismo numero
        for (int i = 0; i < mesas.size(); i++) {

            if (mesas.get(i).getNumeroMesa() == mesa.getNumeroMesa()) {

                // Si existe, reemplaza sus datos
                mesas.set(i, mesa);
                encontrada = true;
                break;
            }
        }

        // Si no existe, agrega la nueva mesa
        if (!encontrada) {
            mesas.add(mesa);
        }

        // Guarda la lista actualizada
        saveAll(mesas);

        return mesa;
    }

    // Metodo para buscar una mesa por numero
    public synchronized Optional<Mesa> findByNumeroMesa(int numeroMesa) {

        return findAll().stream()
                .filter(m -> m.getNumeroMesa() == numeroMesa)
                .findFirst();
    }

    // Metodo para eliminar una mesa
    public synchronized boolean deleteByNumeroMesa(int numeroMesa) {

        List<Mesa> mesas = findAll();

        // Elimina la mesa que coincida con el numero
        boolean removido = mesas.removeIf(
                m -> m.getNumeroMesa() == numeroMesa);

        // Si se elimino, actualiza el archivo JSON
        if (removido) {
            saveAll(mesas);
        }

        return removido;
    }
}