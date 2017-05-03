package com.slavov.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slavov.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
