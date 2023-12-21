package co.com.online_trainer.mongo.messagefailed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collation = "messageFailed")
public class MessageFailedData {

    private String correlationId;
    private Object message;
    private String deliveryNonCause;
    private String executedRetries;
    private LocalDateTime dateTime;
}
