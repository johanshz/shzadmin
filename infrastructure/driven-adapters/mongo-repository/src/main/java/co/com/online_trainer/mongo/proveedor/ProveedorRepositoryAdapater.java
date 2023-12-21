package co.com.online_trainer.mongo.proveedor;


import co.com.online_trainer.model.proveedor.Proveedor;
import co.com.online_trainer.model.proveedor.gateway.ProveedorRepository;
import co.com.online_trainer.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log
public class ProveedorRepositoryAdapater extends AdapterOperations<Proveedor,ProveedorData,String,ProveedorDBRepository> implements ProveedorRepository {
    public  ProveedorRepositoryAdapater(ProveedorDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d->mapper.map(d, Proveedor.class));
    }


    @Override
    public Mono<Proveedor> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }
}
