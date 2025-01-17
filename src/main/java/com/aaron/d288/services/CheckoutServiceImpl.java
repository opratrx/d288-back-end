package com.aaron.d288.services;

import com.aaron.d288.dao.*;
import com.aaron.d288.entities.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    // Autowiring repositories to interact with the database
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final VacationRepository vacationRepository;
    private final ExcursionRepository excursionRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository, CartRepository cartRepository,
                               VacationRepository vacationRepository, ExcursionRepository excursionRepository,
                               CartItemRepository cartItemRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.vacationRepository = vacationRepository;
        this.excursionRepository = excursionRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        try {
            // Generate a unique tracking number for the order
            String orderTrackingNumber = generateOrderTrackingNumber();
            purchase.getCart().setOrderTrackingNumber(orderTrackingNumber);

            // Set the status of the cart to ordered
            purchase.getCart().setStatus(StatusType.CartStatus.ordered);

            // Extract the first vacation from the cart items and throw an exception if it's null
            Vacation vacation = purchase.getCartItems()
                    .stream()
                    .findFirst()
                    .map(CartItem::getVacation)
                    .orElseThrow(() -> new IllegalArgumentException("Null Error: Vacation is null."));

            // Save the vacation details to the repository
            vacationRepository.save(vacation);

            // Save the cart with its updated status and tracking number
            Cart savedCart = cartRepository.save(purchase.getCart());

            // Loop through excursions related to the vacation and save them
            Optional.ofNullable(vacation.getExcursions())
                    .ifPresent(excursions -> excursions.forEach(excursion -> {
                        if (excursion.getVacation() == null) {
                            excursion.setVacation(vacation);
                        }
                        // Save each excursion
                        excursionRepository.save(excursion);
                    }));

            // Associate each cart item with the saved cart and save them
            purchase.getCartItems().forEach(cartItem -> {
                cartItem.setCart(savedCart);
                cartItemRepository.save(cartItem);
            });

            // For each cart item, update the associated excursions and save the updated excursions
            purchase.getCartItems().forEach(cartItem -> {
                Set<Excursion> excursionsForCartItem = cartItem.getExcursions();
                if (excursionsForCartItem != null) {
                    excursionsForCartItem.forEach(excursion -> {
                        Excursion persistedExcursion = excursionRepository.findById(excursion.getId()).orElse(null);
                        if (persistedExcursion != null) {
                            persistedExcursion.getCartItems().add(cartItem);
                            excursionRepository.save(persistedExcursion);
                        }
                    });
                }
            });

            // Save the customer information last, ensuring all related entities are updated first
            Customer customer = purchase.getCustomer();
            customerRepository.save(customer);

            return new PurchaseResponse(orderTrackingNumber);
        } catch (Exception exception) {
            // Return the error message in case of exceptions during the order placement
            return new PurchaseResponse(exception.getMessage());
        }
    }

    // Method to generate a unique order tracking number using UUID
    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
