package co.com.online_trainer.mongo.categoria;

import co.com.online_trainer.model.categoria.Categoria;
import co.com.online_trainer.model.categoria.gateway.CategoriaRepository;
import co.com.online_trainer.model.proveedor.Proveedor;
import co.com.online_trainer.mongo.helper.AdapterOperations;
import co.com.online_trainer.mongo.proveedor.ProveedorDBRepository;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log
public class CategoriaRepositoryAdapter  extends AdapterOperations<Categoria,CategoriaData,String,CategoriaDBRepository> implements CategoriaRepository {
    public  CategoriaRepositoryAdapter(CategoriaDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d->mapper.map(d, Categoria.class));
    }


    @Override
    public Mono<Categoria> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }
}
