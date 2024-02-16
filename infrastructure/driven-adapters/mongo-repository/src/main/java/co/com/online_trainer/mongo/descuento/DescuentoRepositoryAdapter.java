package co.com.online_trainer.mongo.descuento;

import co.com.online_trainer.model.descuento.Descuento;
import co.com.online_trainer.model.descuento.gateway.DescuentoRepository;
import co.com.online_trainer.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
@Log
public class DescuentoRepositoryAdapter extends AdapterOperations<Descuento,DescuentoData,String,DescuentoDBRepository> implements DescuentoRepository {

    public DescuentoRepositoryAdapter(DescuentoDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d -> mapper.map(d,Descuento.class));
    }
}
