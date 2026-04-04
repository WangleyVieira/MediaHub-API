package com.mediahub.mediahub_api.service;

import com.mediahub.mediahub_api.dto.request.AlbumRequest;
import com.mediahub.mediahub_api.dto.response.AlbumResponse;
import com.mediahub.mediahub_api.dto.response.UserResumeResponse;
import com.mediahub.mediahub_api.model.Album;
import com.mediahub.mediahub_api.model.User;
import com.mediahub.mediahub_api.model.UserAlbum;
import com.mediahub.mediahub_api.repository.AlbumRepository;
import com.mediahub.mediahub_api.repository.UserAlbumRepository;
import com.mediahub.mediahub_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final UserAlbumRepository userAlbumRepository;

    @Transactional
    public AlbumResponse create(AlbumRequest albumRequest) {

        Set<User> users = validateUsers(albumRequest.userIds());

        Album album = new Album();
        album.setTitle(albumRequest.title());
        album.setReleaseDate(albumRequest.releaseDate());
        album.setCreatedAt(LocalDateTime.now());
        album.setUpdatedAt(LocalDateTime.now());

        Album savedAlbum = albumRepository.save(album);

        saveUserRelations(savedAlbum, users);

        return albumResponse(savedAlbum);
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponse> getAll(Pageable pageable) {

       Page<Album> page = albumRepository.findAll(pageable);

       return page.map(this::albumResponse);
    }

    @Transactional(readOnly = true)
    public AlbumResponse findById(Long id) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Album not found")
                );

        return albumResponse(album);
    }

    @Transactional
    public void delete(Long id) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        albumRepository.delete(album);
    }

    @Transactional
    public AlbumResponse update(Long id, AlbumRequest albumRequest) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        Set<User> users = validateUsers(albumRequest.userIds());

        album.setTitle(albumRequest.title());
        album.setReleaseDate(albumRequest.releaseDate());
        album.setUpdatedAt(LocalDateTime.now());

        Album savedAlbum = albumRepository.save(album);

        //remove old relationships
        List<UserAlbum> relations = userAlbumRepository.findByAlbumId(album.getId());
        userAlbumRepository.deleteAll(relations);

        saveUserRelations(savedAlbum, users);

        return albumResponse(savedAlbum);
    }

    // =====================================================
    // HELPERS
    // ======================================================

    private AlbumResponse albumResponse(Album album) {
        Set<UserResumeResponse> users = userAlbumRepository
                .findByAlbumId(album.getId())
                .stream()
                .map(u -> new UserResumeResponse(
                        u.getUser().getId(),
                        u.getUser().getName()
                ))
                .collect(Collectors.toSet());

        return new AlbumResponse(
                album.getId(),
                album.getTitle(),
                album.getReleaseDate(),
                users
        );
    }

    private Set<User> validateUsers(Set<UUID> userIds) {

        Set<User> users = new HashSet<>(userRepository.findAllById(userIds));

        if (users.size() != userIds.size()) {
            throw new IllegalArgumentException("One or more user IDs do not match");
        }

        return users;
    }

    private void saveUserRelations(Album album, Set<User> users) {

        for (User user : users) {
            UserAlbum relation = new UserAlbum();
            relation.setUser(user);
            relation.setAlbum(album);

            userAlbumRepository.save(relation);
        }
    }
}
