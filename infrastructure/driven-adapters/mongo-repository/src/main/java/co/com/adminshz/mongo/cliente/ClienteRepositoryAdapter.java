package co.com.adminshz.mongo.cliente;

import co.com.adminshz.model.cliente.Cliente;
import co.com.adminshz.model.cliente.gateway.ClienteRepository;
import co.com.adminshz.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
@Log
public class ClienteRepositoryAdapter extends AdapterOperations<Cliente,ClienteData,String,ClienteDBRepository> implements ClienteRepository {

    public ClienteRepositoryAdapter(ClienteDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d -> mapper.map(d,Cliente.class));
    }
    @Override
    public Mono<Cliente> findByIdenficacion(String identificacion) {
        return repository.findByIdentificacion(identificacion);
    }

    @Override
    public Mono<Cliente> findByTelefono(Long telefono) {
        return repository.findByTelefono(telefono);
    }
}
