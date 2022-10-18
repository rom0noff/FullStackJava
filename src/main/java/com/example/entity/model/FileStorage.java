package com.example.entity.model;

import com.example.entity.enums.FileStorageStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class FileStorage implements Serializable {
    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String extension;
    private String contentType;
    private Long fileSize;
    private FileStorageStatus fileStorageStatus;
    private String hashId;
    private String uploadFolder;

}
