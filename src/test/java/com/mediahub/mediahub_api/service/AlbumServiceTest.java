package com.mediahub.mediahub_api.service;

import com.mediahub.mediahub_api.dto.request.AlbumRequest;
import com.mediahub.mediahub_api.dto.response.AlbumResponse;
import com.mediahub.mediahub_api.model.Album;
import com.mediahub.mediahub_api.model.User;
import com.mediahub.mediahub_api.model.UserAlbum;
import com.mediahub.mediahub_api.repository.AlbumRepository;
import com.mediahub.mediahub_api.repository.UserAlbumRepository;
import com.mediahub.mediahub_api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for the Album service
 * Based on the business rule validation and dependency isolation pattern
 */
@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAlbumRepository userAlbumRepository;

    @InjectMocks
    private AlbumService albumService;

    @DisplayName("You must create the album successfully")
    @Test
    void shouldCreateAlbumSuccessfully() {

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Artist test");

        AlbumRequest albumRequest = new AlbumRequest("New album", LocalDate.now(), Set.of(userId));

        Album savedAlbum = new Album();
        savedAlbum.setId(1L);
        savedAlbum.setTitle(albumRequest.title());

        when(userRepository.findAllById(albumRequest.userIds())).thenReturn(List.of(user));
        when(albumRepository.save(any(Album.class))).thenReturn(savedAlbum);

        when(userAlbumRepository.findByAlbumId(any())).thenReturn(List.of());

        AlbumResponse albumResponse = albumService.create(albumRequest);

        assertNotNull(albumResponse);
        assertEquals("New album", albumResponse.title());
        verify(albumRepository).save(any(Album.class));
        verify(userAlbumRepository).save(any(UserAlbum.class));
    }

    @DisplayName("An exception should be throw when a user is not found in the database")
    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCreate() {

        AlbumRequest albumRequest = new AlbumRequest("Album", LocalDate.now(), Set.of(UUID.randomUUID()));
        when(userRepository.findAllById(any())).thenReturn(List.of()); // return empty list to simulate user not found


        assertThrows(IllegalArgumentException.class, () -> albumService.create(albumRequest));
        verify(albumRepository, never()).save(any());
    }

    @DisplayName("You should be able to find the album by ID successfully")
    @Test
    void shouldFindAlbumByIdSuccessfully() {

        Album album = new Album();
        album.setId(1L);
        album.setTitle("Gold album");

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(userAlbumRepository.findByAlbumId(1L)).thenReturn(List.of());

        AlbumResponse albumResponse = albumService.findById(1L);

        assertEquals("Gold album", albumResponse.title());
        verify(albumRepository).findById(1L);
    }

    @DisplayName("An exception should be made when searching for a non-existent album.")
    @Test
    void shouldThrowExceptionWhenAlbumNotFound() {

        when(albumRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> albumService.findById(99L)
        );
        assertEquals("Album not found", exception.getMessage());
    }

    @DisplayName("You should successfully delete the album.")
    @Test
    void shouldDeleteAlbumSuccessfully() {

        Album album = new Album();
        album.setId(1L);
        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        albumService.delete(1L);

        verify(albumRepository).delete(album);
    }

    @DisplayName("You should update the album successfully, resetting relationships.")
    @Test
    void shouldUpdateAlbumSuccessfully() {

        Long albumId = 1L;
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Album existingAlbum = new Album();
        existingAlbum.setId(albumId);

        AlbumRequest updateRequest = new AlbumRequest("Updated Title", LocalDate.now(), Set.of(userId));

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(existingAlbum));
        when(userRepository.findAllById(any())).thenReturn(List.of(user));
        when(albumRepository.save(any())).thenReturn(existingAlbum);
        when(userAlbumRepository.findByAlbumId(albumId)).thenReturn(List.of());

        AlbumResponse response = albumService.update(albumId, updateRequest);

        assertEquals("Updated Title", response.title());
        verify(userAlbumRepository).deleteAll(any()); // ensures that ond relationships have been removed
        verify(userAlbumRepository).save(any());      // he assures that the new ones where saved
    }

    @DisplayName("You must return paginated albums.")
    @Test
    void shouldReturnPagedAlbums() {

        Pageable pageable = PageRequest.of(0, 10);
        Album album = new Album();
        album.setId(1L);
        Page<Album> albumPage = new PageImpl<>(List.of(album));

        when(albumRepository.findAll(pageable)).thenReturn(albumPage);
        when(userAlbumRepository.findByAlbumId(any())).thenReturn(List.of());

        Page<AlbumResponse> result = albumService.getAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(albumRepository).findAll(pageable);
    }
}