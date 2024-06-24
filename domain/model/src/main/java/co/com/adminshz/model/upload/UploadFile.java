package co.com.adminshz.model.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class UploadFile<T>{
    private T file;
}
