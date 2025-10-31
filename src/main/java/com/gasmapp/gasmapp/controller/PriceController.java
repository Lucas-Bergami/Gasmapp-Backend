package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.PriceModel;
import com.gasmapp.gasmapp.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public List<PriceModel> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceModel> getPriceById(@PathVariable Long id) {
        Optional<PriceModel> price = priceService.getPriceById(id);
        return price.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PriceModel> createPrice(@RequestBody PriceModel price) {
        PriceModel savedPrice = priceService.createPrice(price);
        return ResponseEntity.status(201).body(savedPrice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceModel> updatePrice(@PathVariable Long id, @RequestBody PriceModel updatedPrice) {
        Optional<PriceModel> price = priceService.updatePrice(id, updatedPrice);
        return price.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        boolean deleted = priceService.deletePrice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadPriceImage(@RequestParam("file") MultipartFile file) {
        try {
            // Salva o arquivo temporariamente
            File tempFile = File.createTempFile("price_", ".jpg");
            file.transferTo(tempFile);

            // Caminho do script Python
            String scriptPath = "/app/scripts/process_price_image.py"; // ajustar

            // Executa o script Python
            ProcessBuilder pb = new ProcessBuilder("python3", scriptPath, tempFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Lê a saída do Python
            String output = new String(process.getInputStream().readAllBytes());

            int exitCode = process.waitFor();

            // Deleta o arquivo temporário
            tempFile.delete();

            if (exitCode == 0) {
                return ResponseEntity.ok(output);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao processar imagem:\n" + output);
            }

        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao executar script Python: " + e.getMessage());
        }
    }
}
