package com.tms.sportlight.repository;

import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final JpaFileRepository jpaFileRepository;

    public Optional<UploadFile> findById(int id) {
        return jpaFileRepository.findById(id);
    }

    public void save(UploadFile uploadFile) {
        jpaFileRepository.save(uploadFile);
    }

    public List<UploadFile> findByFileTypeAndFileIdentifier(FileType type, int identifier) {
        return jpaFileRepository.findByTypeAndIdentifier(type, identifier);
    }

    public Optional<UploadFile> findRecentFile(FileType type, int identifier){
        return jpaFileRepository.findRecentFile(type, identifier);
    };
}
