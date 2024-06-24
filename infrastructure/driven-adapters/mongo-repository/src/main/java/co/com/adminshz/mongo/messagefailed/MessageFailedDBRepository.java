package co.com.adminshz.mongo.messagefailed;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MessageFailedDBRepository extends ReactiveMongoRepository<MessageFailedData, String>, ReactiveQueryByExampleExecutor<MessageFailedData> {
}
