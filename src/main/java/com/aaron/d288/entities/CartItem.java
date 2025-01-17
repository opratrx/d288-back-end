package com.aaron.d288.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart_items")
@Getter
@Setter

public class CartItem {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vacation_id")
    private Vacation vacation;

    @ManyToMany(mappedBy = "cartItems")
    private Set<Excursion> excursions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @CreationTimestamp
    @Column(name = "create_date")
    private Date create_date;

    @UpdateTimestamp
    @Column(name = "last_update")
    private Date last_update;

    public void addExcursion(Excursion excursion)
    {
        this.excursions.add(excursion);
        excursion.getCartItems().add(this);
    }
}
