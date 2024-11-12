package com.tms.sportlight.repository;

import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFileRepository extends JpaRepository<UploadFile, Integer> {

    List<UploadFile> findByTypeAndIdentifier(FileType fileType, int fileIdentifier);

}
