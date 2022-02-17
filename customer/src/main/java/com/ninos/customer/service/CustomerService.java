package com.ninos.customer.service;

import com.ninos.amqp.RabbitMQMessageProducer;
import com.ninos.clients.fraud.FraudCheckResponse;
import com.ninos.clients.fraud.FraudClient;
import com.ninos.clients.notification.NotificationClient;
import com.ninos.clients.notification.NotificationRequest;
import com.ninos.customer.model.Customer;
import com.ninos.customer.repository.CustomerRepo;
import com.ninos.customer.response.CustomerRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;
//    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
//    private final NotificationClient notificationClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;


    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken

        customerRepo.saveAndFlush(customer);

        // todo: check if fraudster
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId());
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());


        if (fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }


        // todo: make it async. i.e add to queue
//        notificationClient.sendNotification(
//                new NotificationRequest(
//                        customer.getId(),
//                        customer.getEmail(),
//                        String.format("Hi %s, welcome to Ankedoscode...",
//                                customer.getFirstName())
//                )
//        );

         NotificationRequest notificationRequest = new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Ankedoscode...", customer.getFirstName())
         );

        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }


}
