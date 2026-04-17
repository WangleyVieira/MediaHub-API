package com.mediahub.mediahub_api.repository;

import com.mediahub.mediahub_api.model.AlbumCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumCoverRepository extends JpaRepository<AlbumCover, Long> {

    Optional<AlbumCover> findByAlbumId(Long id);

}
