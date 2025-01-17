package com.aaron.d288.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "customer_first_name", nullable = false)
    String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "customer_last_name", nullable = false)
    String lastName;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "address", nullable = false)
    String address;

    @NotNull
    @Pattern(regexp = "^[0-9]{5}$")
    @Column(name = "postal_code", nullable = false)
    String postal_code;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    @Column(name = "phone", nullable = false)
    String phone;

    @ManyToOne
    @JoinColumn(name="division_id")
    Division division;

    @OneToMany(mappedBy = "customer")
    Set<Cart> carts = new HashSet<Cart>();

    @CreationTimestamp
    @Column(name = "create_date")
    Date create_date;

    @UpdateTimestamp
    @Column(name = "last_Update")
    Date last_update;

    public void add(Cart cart) {
        if (cart != null) {
            if (carts == null) {
                carts = new HashSet<>();
            }
            carts.add(cart);
            cart.setCustomer(this);
        }
    }
}
