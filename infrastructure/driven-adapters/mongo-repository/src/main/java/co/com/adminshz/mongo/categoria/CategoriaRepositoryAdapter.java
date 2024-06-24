package co.com.adminshz.mongo.categoria;

import co.com.adminshz.model.categoria.Categoria;
import co.com.adminshz.model.categoria.gateway.CategoriaRepository;
import co.com.adminshz.mongo.helper.AdapterOperations;
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
