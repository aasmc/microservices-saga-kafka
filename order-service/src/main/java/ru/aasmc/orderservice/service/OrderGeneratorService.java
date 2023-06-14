package ru.aasmc.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.aasmc.base.domain.Order;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderGeneratorService {
    private final Executor taskExecutor;
    private final KafkaTemplate<Long, Order> template;

    private AtomicLong id = new AtomicLong();
    private static Random RAND = new Random();

    @Async
    public void generate() {
        for (int i = 0; i < 10000; i++) {
            int x = RAND.nextInt(5) + 1;
            Order o = new Order(id.incrementAndGet(),
                    RAND.nextLong(100) + 1,
                    RAND.nextLong(100) + 1,
                    "NEW");
            o.setPrice(100 * x);
            o.setProductCount(x);
            template.send("orders", o.getId(), o);
        }
    }
}
