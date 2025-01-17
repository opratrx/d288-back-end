package com.aaron.d288.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StatusType {
    CartStatus cartStatus;

    public enum CartStatus{
        pending, ordered, canceled
    }

    public StatusType(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }
}
