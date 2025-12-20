package com.porramat081.e_menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime stopDate;

    private String fileName;
    private String fileType;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] image;

    private String downloadUrl;
}
