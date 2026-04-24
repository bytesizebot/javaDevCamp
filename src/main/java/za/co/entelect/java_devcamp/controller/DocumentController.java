package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.java_devcamp.entity.ContractDocument;
import za.co.entelect.java_devcamp.repository.DocumentRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/contract")
@Tag(name = "Contract")
@AllArgsConstructor
public class DocumentController {
    private  final DocumentRepository documentRepository;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws Exception {


        ContractDocument doc = documentRepository.findByFileName(fileName)
                .orElseThrow();

        Path path = Paths.get(doc.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
