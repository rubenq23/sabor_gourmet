package cl.ipss.SaborGourmet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.ipss.SaborGourmet.repository.ClienteRepository;
import cl.ipss.SaborGourmet.model.Cliente;


@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    //iyeccion de dependencia por metodo constructor
    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    //metodo para obtener todo los clientes
    public List<Cliente> obtenerTodos() {  //devuelve una lista de tipo Cliente, con todos los clientes o una lista vacia si no hay ninguno.
        return clienteRepository.findAll(); //no lee el archivo json directamente, se lo delega al repositorio, a traves de clienteRepository.
    }

    //metodo para buscar cliente por id
    public Cliente buscarIdCliente(int id){ //devuelve un objeto de tipo Cliente
        return clienteRepository.findbyId(id)  //busca el cliente por su identificador usando al repositorio mediante clienteRepository.
        .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado")); //si el id del cliente no existe, lanza una RuntimeException con mensaje de error.
    }

    //metodo para crear cliente
    public Cliente crearcliente(Cliente cliente){ //devuelve un objeto de tipo Cliente
        return clienteRepository.save(cliente); //delega la generacion del id y la persistencia al repositorio usando lienteRepository.
    }

    //metodo para actualizar cliente
    public Cliente actualizarCliente(int id, Cliente clienteActualizado){
        //primero verifica que el cliente exista o laza una excepcion
        Cliente clienteExistente = buscarIdCliente(id);

        //actualiza los datos usando clienteExistente para setear los datos que llegan a traves de clienteActualziado
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setEmail(clienteActualizado.getNombre());

        //sobreescribe los datos y los guarda en el archivo json usando clienteRepository.save()
        return clienteRepository.save(clienteExistente);
    }

    //metodo para eliminar un cliente
    public void eliminarCliente(int id){
        //verifica que existe el id del cliente
        buscarIdCliente(id);
        //eliminar el cliente a partir del identificador
        clienteRepository.deleteById(id);
    }

}
