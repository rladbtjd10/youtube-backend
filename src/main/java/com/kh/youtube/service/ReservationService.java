package com.kh.youtube.service;

import com.kh.youtube.domain.Reservation;
import com.kh.youtube.repo.ReservationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    public List<Reservation> showAll() {
        return reservationDAO.findAll();
    }

    public Reservation show(int id) {
        return reservationDAO.findById(id).orElse(null);
    }

    public Reservation create(Reservation reservation) {
        return reservationDAO.save(reservation);
    }

    public Reservation update(Reservation reservation) {
        Reservation target = reservationDAO.findById(reservation.getReserCode()).orElse(null);
        if (target != null) {
            return reservationDAO.save(reservation);
        }
        return null;
    }

    public Reservation delete(int id) {
        Reservation target = reservationDAO.findById(id).orElse(null);
        reservationDAO.delete(target);
        return target;
    }
}