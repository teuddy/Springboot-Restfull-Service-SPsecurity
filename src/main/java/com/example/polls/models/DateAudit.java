package com.example.polls.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

/*Let’s suppose we have a File entity and a single record in file table
stores name and content of the file and we also want to store who created and modified
any file at what time. So we can keep track like when the file was created by whom and
when it was last modified by whom.*/


@MappedSuperclass// esto significa que los atributos de esta clase se crearan bajo la tabla de sus subclases.
@EntityListeners(AuditingEntityListener.class)//aqui le indic
public class DateAudit implements Serializable {


    @CreatedDate//cuando se creo?
    @Column(nullable = false, updatable = false)// no debe ser nulo ni tampoco puede ser updated
    public Instant createdAt;


    @LastModifiedDate// ultima modificacion
    @Column(nullable = false)
    public Instant updatedAt;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

