package com.mediahub.mediahub_api.repository;

import com.mediahub.mediahub_api.model.UserAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAlbumRepository extends JpaRepository<UserAlbum,Long> {

    List<UserAlbum> findByUserId(UUID userId);

    List<UserAlbum> findByAlbumId(Long albumId);

    boolean existsByUserIdAndAlbumId(UUID userId, Long albumId);
}
