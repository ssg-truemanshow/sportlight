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

    @Query("SELECT u FROM UploadFile u"
            + " WHERE u.type = :type AND u.identifier = :identifier AND u.deleted = false"
            + " ORDER BY u.regDate DESC LIMIT 1")
    Optional<UploadFile> findRecentFile(@Param("type") FileType fileType, @Param("identifier") int fileIdentifier);

    @Query("SELECT uf FROM UploadFile uf WHERE uf.type = :fileType AND uf.identifier = :fileIdentifier AND uf.deleted = false ORDER BY uf.regDate DESC")
    List<UploadFile> findByTypeAndIdentifierOrderByRegDateDesc(
        @Param("fileType") FileType fileType,
        @Param("fileIdentifier") Integer fileIdentifier
    );

}

