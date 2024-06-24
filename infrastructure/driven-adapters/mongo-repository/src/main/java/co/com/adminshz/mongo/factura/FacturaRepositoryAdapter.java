package co.com.adminshz.mongo.factura;

import co.com.adminshz.model.factura.Factura;
import co.com.adminshz.model.factura.gateway.FacturaRepository;
import co.com.adminshz.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log
public class FacturaRepositoryAdapter extends AdapterOperations<Factura,FacturaData,String,FacturaDBRepository> implements FacturaRepository {

    public FacturaRepositoryAdapter(FacturaDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d -> mapper.map(d,Factura.class));
    }

    @Override
    public Mono<Factura> findByIdCompra(Integer idCompra) {
        return repository.findByIdCompra(idCompra);
    }

    @Override
    public Mono<Factura> findByIdenficacion(String identificacion) {
        return repository.findByIdentificacion(identificacion);
    }

    @Override
    public Mono<Factura> findByTelefono(Long telefono) {
        return repository.findByTelefono(telefono);
    }
}
