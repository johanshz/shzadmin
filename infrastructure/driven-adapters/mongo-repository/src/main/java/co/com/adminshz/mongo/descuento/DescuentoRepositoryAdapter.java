package co.com.adminshz.mongo.descuento;

import co.com.adminshz.model.descuento.Descuento;
import co.com.adminshz.model.descuento.gateway.DescuentoRepository;
import co.com.adminshz.mongo.helper.AdapterOperations;
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
