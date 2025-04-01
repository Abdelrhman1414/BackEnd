package com.BackEnd.BidPro.Model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductBiderId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long productId;
    private Long userId;

    public ProductBiderId() {

    }
    public ProductBiderId(Long productId, Long userId) {
        super();
        this.productId = productId;
        this.userId = userId;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result
                + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductBiderId other = (ProductBiderId) obj;
        return Objects.equals(getProductId(), other.getProductId()) && Objects.equals(getUserId(), other.getUserId());
    }

}