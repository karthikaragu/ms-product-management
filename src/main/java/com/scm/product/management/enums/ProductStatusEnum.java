package com.scm.product.management.enums;

public enum ProductStatusEnum {

    AVAILABLE("Y"),
    OUTOFSTOCK("N");


    public final String code;
    private ProductStatusEnum(String code){
        this.code = code;
    }
}
