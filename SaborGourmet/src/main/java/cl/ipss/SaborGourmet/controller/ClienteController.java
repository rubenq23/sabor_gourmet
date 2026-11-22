package cl.ipss.SaborGourmet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.ipss.SaborGourmet.service.ClienteService;
import jakarta.validation.Valid;
import cl.ipss.SaborGourmet.model.Cliente;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    //inyeccion de dependencia por constructor
    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }
     //metodo get para listar todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(){
        //devuelve una lista de tipo cliente, llamada clientes, que usa el servicio para acceder al metodo obtenerTodos()
        List<Cliente> clientes = clienteService.obtenerTodos();
        //devuelve un HTTP 200 OK
        return ResponseEntity.ok(clientes);
    }

    //metodo para buscar cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCliente(@PathVariable int id){
        try{
             Cliente cliente = clienteService.buscarIdCliente(id);
        return ResponseEntity.ok(cliente);
        //si el servicio no encuentra el id y lanza error not found, devuelve HTTP 404
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //metodo para crear un cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente){
        Cliente nuevoCliente = clienteService.crearcliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    //metodo para actualizar un cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable int id, @Valid @RequestBody Cliente cliente){
        try{
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteActualizado); // actualiza el cliente y devuelve un 200 OK
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //metodo para eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable int id){
        try{
            clienteService.eliminarCliente(id);
            //HTTP 204 es el estandar para cuando se elimina algo exitosamente
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
