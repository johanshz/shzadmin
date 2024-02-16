package co.com.online_trainer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class DescuentoDto {
    private String id;
    private Integer valorMin;
    private Integer valorMax;
    private Integer valorDescuento;
}
