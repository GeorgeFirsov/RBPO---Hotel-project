package com.example.service;

import com.example.model.Payment;
import com.example.storage.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    public PaymentService(PaymentRepository paymentRepository) { this.paymentRepository = paymentRepository; }

    public List<Payment> getAllPayments() { return paymentRepository.findAll(); }

    public Payment getPaymentById(Long id) { return paymentRepository.findById(id).orElse(null); }

    public Payment createPayment(Long bookingId, int guestId, BigDecimal amount) {
        Payment p = new Payment(null, bookingId, guestId, amount);
        return paymentRepository.save(p);
    }

    public Payment updatePayment(Long id, BigDecimal amount) {
        Payment existing = paymentRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setAmount(amount);
        return paymentRepository.save(existing);
    }

    public boolean deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) return false;
        paymentRepository.deleteById(id);
        return true;
    }
}
