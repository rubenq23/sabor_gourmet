package cl.ipss.SaborGourmet.repository;

import java.io.File;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cl.ipss.SaborGourmet.model.Cliente;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Repository
public class ClienteRepository {

    
    private final String archivo;
    private final ObjectMapper objectMapper;
    
    public ClienteRepository(@Value("${app.archivo.cliente}") String archivo){
        this.archivo = archivo;
        this.objectMapper = new ObjectMapper();
    }
    
    //metodo para obtener todos los clientes
    public List<Cliente> findAll(){
        File file = new File(archivo);
        if (!file.exists()) {
            return new ArrayList<>(); //si el archivo no existe devuelve una lista vacia
        }
        return objectMapper.readValue(file, new TypeReference<List<Cliente>>() {});
    }
    //metodo para sobreescribir el archivo
    private void saveAll(List<Cliente> clientes){
        
        File file = new File(archivo);
        file.getParentFile().mkdirs(); //verifica que el directorio data exista
        objectMapper.writeValue(file, clientes);
    }

    //metodo para crear un cliente, si este ya existe lo reemplaza.
    public Cliente save(Cliente cliente){
        List<Cliente> clientes = findAll();

        if(cliente.getIdCliente() == null || cliente.getIdCliente() == 0){
            //si es un cliente nuevo, genera un id autoincremental, esto es porque como no estoy trabajando
            //con jpa no tengo manera se generar un id automaticamente
            int nextId = clientes.stream()
                    .mapToInt(Cliente::getIdCliente)
                    .max()
                    .orElse(0) + 1;
            cliente.setIdCliente(nextId);
            clientes.add(cliente);
        } else {
            //si al buscar el id del cliente este ya existe lo reemplaza
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getIdCliente().equals(cliente.getIdCliente())) {
                    clientes.set(i, cliente);
                    break;
                }
            }
        }
        saveAll(clientes); //sobreescribe el json con la lista actualizada del clientes
        return cliente;
    }

   //metodo para buscar cliente por id.
    public Optional<Cliente> findbyId(int id){
        return findAll().stream()
        .filter(c -> c.getIdCliente().equals(id))
        .findFirst();
    }

    //metodo para eliminar el cliente
    public boolean deleteById (int id){
        List<Cliente> clientes = findAll();
        // removeIf elimina el elemento si cumple la condición y devuelve true si lo borro
        boolean removido = clientes.removeIf(c -> c.getIdCliente().equals(id));
        if (removido) {
            saveAll(clientes); // Guarda la lista actualizada sin ese cliente
        }
        return removido;
    }
}
