package co.com.adminshz.mongo.contenidoapp;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface ContenidoAppDBRepository extends ReactiveMongoRepository<ContenidoAppData,String>, ReactiveQueryByExampleExecutor<ContenidoAppData> {
}
