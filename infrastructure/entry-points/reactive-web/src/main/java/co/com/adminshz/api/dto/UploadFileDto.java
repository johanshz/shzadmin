package co.com.adminshz.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.io.File;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public  class UploadFileDto<T> {
    private T file;
}
