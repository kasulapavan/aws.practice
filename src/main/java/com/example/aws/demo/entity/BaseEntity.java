package com.example.aws.demo.entity;//package com.example.aws.demo.entity;
//
//
////import jakarta.persistence.Column;
////import jakarta.persistence.GeneratedValue;
////import jakarta.persistence.GenerationType;
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import org.springframework.data.annotation.CreatedBy;
////import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.LastModifiedBy;
//import jakarta.persistence.*;
//
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.Date;
//
//@Getter
//@Setter
//public  abstract class BaseEntity implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @CreatedBy
//    private Long createdBy;
//
//
//    @LastModifiedBy
//    private Long updatedBy;
//
//    @Column(name = "created_on", updatable = false)
//
//    @CreationTimestamp
//    private Timestamp createdOn = new Timestamp(new Date().getTime());
//
//
//    @Column(name = "updated_on")
//    @UpdateTimestamp
//
//    private Timestamp updatedOn = new Timestamp(new Date().getTime());
//
//    @Column(columnDefinition = "boolean default false")
//    private Boolean isDeleted = Boolean.FALSE;
//
//    @Column(columnDefinition = "boolean default true")
//    private Boolean isActive = Boolean.TRUE;
//
//}


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@MappedSuperclass
@Setter
@Getter
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdOn;
    private LocalDateTime updatedOn;
    private String createdBy;
    private String updatedBy;
    private boolean isActive = Boolean.TRUE;
}