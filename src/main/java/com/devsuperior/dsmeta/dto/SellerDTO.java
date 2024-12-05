package com.devsuperior.dsmeta.dto;

public class SellerDTO {

    private String sellerName;
    private Double invoicing;

    public SellerDTO() {
    }

    public SellerDTO(String sellerName, Double invoicing) {
        this.sellerName = sellerName;
        this.invoicing = invoicing;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Double getInvoicing() {
        return invoicing;
    }

    public void setInvoicing(Double invoicing) {
        this.invoicing = invoicing;
    }
}
