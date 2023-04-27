package com.dev.gware.customboard.post.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IMG_FILE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImgFile {
    @Id
    @NonNull
    @Column(name = "STORE_FILE_NAME")
    String storeFileName;

    @NonNull
    @Column(name = "UPLOAD_FILE_NAME")
    String uploadFileName;

    @Column(name = "EXT")
    String ext;

    @NonNull
    @Column(name = "POST_ID")
    long postId;

    @Column(name = "SAVE_DATETIME")
    String saveDatetime;
}
