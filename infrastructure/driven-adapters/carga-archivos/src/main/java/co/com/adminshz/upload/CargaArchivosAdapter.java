package co.com.adminshz.upload;

import co.com.adminshz.model.registro.Response;
import co.com.adminshz.model.upload.Upload;
import co.com.adminshz.model.upload.UploadFile;
import lombok.extern.java.Log;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.Part;
import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@Log
public class CargaArchivosAdapter implements Upload {

    @Override
    public <T> Mono<String> cargaArchivo(UploadFile<T> file) {

        try {
            FilePart filePart = (FilePart) file.getFile();
            Path tempDir = Paths.get("C:/ImagenesAdmin/");
            File tempFile = new File(tempDir.toFile(), filePart.filename());
            return filePart.transferTo(tempFile).thenReturn(tempFile).thenReturn(tempFile.toString());
        } catch (Exception e) {
            throw new IllegalStateException("Error en la carga del archivo", e);
        }

    }
}
