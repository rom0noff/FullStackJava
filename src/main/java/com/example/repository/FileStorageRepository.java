package com.example.repository;

import com.example.entity.model.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    //
    FileStorage findByHashId(String hashId);
}
