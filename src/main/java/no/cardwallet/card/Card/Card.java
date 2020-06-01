package no.cardwallet.card.Card;

import javax.persistence.*;
import java.sql.Date;

@Entity

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardCode;
    private String storeName;
    private Date expiryDate;
    private int balanceInt;
    private String logoImage;
    private Long userId;
    @Transient
    private boolean isExpired;

    public Card(String storeName, String cardCode, int balanceInt, Date expiryDate, String logoImage, boolean isExpired) {
        this.storeName = storeName;
        this.cardCode = cardCode;
        this.balanceInt = balanceInt;
        this.expiryDate = expiryDate;
        this.logoImage = logoImage;
        this.isExpired = isExpired;
    }

    public Card() {
    }

    public boolean isExpired() { return isExpired; }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) { this.cardCode = cardCode; }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) { this.storeName = storeName; }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getBalanceInt() {
        return balanceInt / 100;
    }

    public void setBalanceInt(int balanceInt) {
        this.balanceInt = balanceInt * 100;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) { this.logoImage = logoImage; }
}