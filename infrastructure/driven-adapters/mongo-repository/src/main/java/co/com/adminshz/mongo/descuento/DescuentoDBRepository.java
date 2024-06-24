package co.com.adminshz.mongo.descuento;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface DescuentoDBRepository extends ReactiveMongoRepository<DescuentoData,String>, ReactiveQueryByExampleExecutor<DescuentoData> {

}
