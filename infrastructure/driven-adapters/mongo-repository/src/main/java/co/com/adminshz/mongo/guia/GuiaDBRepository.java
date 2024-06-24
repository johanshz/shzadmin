package co.com.adminshz.mongo.guia;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface GuiaDBRepository extends ReactiveMongoRepository<GuiaData,String> , ReactiveQueryByExampleExecutor<GuiaData> {
}
