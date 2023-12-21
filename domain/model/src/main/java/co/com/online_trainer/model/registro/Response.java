package co.com.online_trainer.model.registro;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private String correlationId;
    private Integer statusCode;
    private String description;
    private T results;
}
