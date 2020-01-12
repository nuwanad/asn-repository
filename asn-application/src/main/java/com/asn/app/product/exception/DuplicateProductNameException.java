package com.asn.app.product.exception;

public class DuplicateProductNameException extends RuntimeException {

    public DuplicateProductNameException() {
        super("Duplicate product name");
    }
}
