package co.com.online_trainer.mongo.guia;

import co.com.online_trainer.model.guia.Guia;
import co.com.online_trainer.model.guia.gateway.GuiaRepository;
import co.com.online_trainer.mongo.helper.AdapterOperations;
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
