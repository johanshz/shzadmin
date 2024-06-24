package co.com.adminshz.mongo.descuento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "descuento")
public class DescuentoData {
    @Id
    private String id;
    private Integer valorMin;
    private Integer valorMax;
    private Integer valorDescuento;
}
