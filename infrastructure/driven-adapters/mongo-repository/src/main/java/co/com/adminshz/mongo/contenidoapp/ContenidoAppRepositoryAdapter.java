package co.com.adminshz.mongo.contenidoapp;

import co.com.adminshz.model.contenidoapp.ContenidoApp;
import co.com.adminshz.model.contenidoapp.gateway.ContenidoAppRepository;
import co.com.adminshz.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
@Log
public class ContenidoAppRepositoryAdapter extends AdapterOperations<ContenidoApp,ContenidoAppData,String,ContenidoAppDBRepository> implements ContenidoAppRepository {

    public ContenidoAppRepositoryAdapter(ContenidoAppDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d -> mapper.map(d, ContenidoApp.class));
    }
}
