package az.company.demo.service;

import az.company.demo.dao.entity.Payment;
import az.company.demo.dao.repository.PaymentRepository;
import az.company.demo.model.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Payment getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElse(null);
    }

    @Transactional
    public Payment createPending(Long orderId, BigDecimal amount) {

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    @Transactional
    public void markSuccess(Long orderId) {

        Payment payment = getExistingPayment(orderId);

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
    }

    @Transactional
    public void markFailed(Long orderId) {

        Payment payment = getExistingPayment(orderId);

        payment.setStatus(PaymentStatus.FAILED);
    }

    private Payment getExistingPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found for order: " + orderId
                        )
                );
    }
}