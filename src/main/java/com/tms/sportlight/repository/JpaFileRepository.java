package com.tms.sportlight.repository;

import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.UploadFile;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFileRepository extends JpaRepository<UploadFile, Integer> {

    @Query("SELECT u FROM UploadFile u"
            + " WHERE u.type = :type AND u.identifier = :identifier AND u.deleted = false"
            + " ORDER BY u.regDate")
    List<UploadFile> findByTypeAndIdentifier(@Param("type") FileType fileType, @Param("identifier") int fileIdentifier);

    @Query(value = "SELECT * FROM upload_file "
        + "         WHERE file_type = ?1 AND file_identifier = ?2 AND deleted = 0 "
        + "         ORDER BY reg_date DESC LIMIT 1", nativeQuery = true)
    Optional<UploadFile> findRecentFile(FileType fileType, int fileIdentifier);

}

