package com.mediahub.mediahub_api.repository;

import com.mediahub.mediahub_api.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {

}
