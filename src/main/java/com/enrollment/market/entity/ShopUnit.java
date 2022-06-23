package com.enrollment.market.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class ShopUnit {
    @Id
//    @GeneratedValue
    private UUID id;

    @Transient
    private UUID parentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Timestamp date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    private Long price = null;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="parent_id")
    @JsonBackReference
    private ShopUnit parent;

    @OneToMany(mappedBy="parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private Set<ShopUnit> children = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParentId() {
        if (parent != null) {
            return parent.getId();
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public ShopUnitType getType() {
        return type;
    }

    public void setType(ShopUnitType type) {
        this.type = type;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public ShopUnit getParent() {
        return parent;
    }

    public void setParent(ShopUnit parent) {
        this.parent = parent;
    }

    public Set<ShopUnit> getChildren() {
        if (type == ShopUnitType.OFFER) {
            return null;
        }

        return children;
    }

    public void addChild(ShopUnit unit) {
        children.add(unit);
    }

    public void removeChild(ShopUnit unit) {
        children.remove(unit);
    }
}
