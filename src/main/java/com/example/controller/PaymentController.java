package com.example.controller;

import com.example.model.Payment;
import com.example.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping("/add")
    public Payment add(@RequestParam Long bookingId, @RequestParam int guestId, @RequestParam BigDecimal amount) {
        return paymentService.createPayment(bookingId, guestId, amount);
    }

    @GetMapping
    public List<Payment> all() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        Payment p = paymentService.getPaymentById(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PutMapping("/update")
    public ResponseEntity<Payment> update(@RequestParam Long id, @RequestParam BigDecimal amount) {
        Payment updated = paymentService.updatePayment(id, amount);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long id) {
        boolean deleted = paymentService.deletePayment(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Payment deleted");
    }
}
