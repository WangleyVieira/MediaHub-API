package com.mediahub.mediahub_api.controller;

import com.mediahub.mediahub_api.dto.request.AlbumRequest;
import com.mediahub.mediahub_api.dto.response.AlbumResponse;
import com.mediahub.mediahub_api.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponse> create (@RequestBody @Valid AlbumRequest albumRequest) {
        AlbumResponse response = albumService.create(albumRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AlbumResponse>> getAll() {
        List<AlbumResponse> listAlbums = albumService.getAll();
        return ResponseEntity.ok(listAlbums);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AlbumResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AlbumResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid AlbumRequest albumRequest
    ) {
        return ResponseEntity.ok(albumService.update(id, albumRequest));
    }

}
