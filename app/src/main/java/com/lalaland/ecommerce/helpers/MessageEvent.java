package com.lalaland.ecommerce.helpers;

public class MessageEvent {

    int position;
    int quantity;

    public MessageEvent(int position, int quantity) {
        this.position = position;
        this.quantity = quantity;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
