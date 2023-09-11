package com.kh.youtube.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @Column(name = "RESER_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reservation")
    @SequenceGenerator(name = "seq_reservation", sequenceName = "SEQ_RESERVATION", allocationSize = 1)
    private int reserCode;

    @Column(name = "RESER_COM")
    private String reserCom;

    @Column(name = "RESER_NO", nullable = false)
    private String reserNo;

    @Column(name = "RESER_TIME", columnDefinition = "TIMESTAMP DEFAULT SYSTIMESTAMP")
    private Timestamp reserTime;

    @Column(name = "MEM_CODE")
    private int memCode;

    @Column(name = "RES_CODE")
    private int resCode;
}