package co.com.adminshz.mongo.guia;

import co.com.adminshz.model.guia.Guia;
import co.com.adminshz.model.guia.gateway.GuiaRepository;
import co.com.adminshz.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
@Log
public class GuiaRepositoryAdapter extends AdapterOperations<Guia,GuiaData,String,GuiaDBRepository> implements GuiaRepository {
    public GuiaRepositoryAdapter(GuiaDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d -> mapper.map(d, Guia.class));
    }

}
