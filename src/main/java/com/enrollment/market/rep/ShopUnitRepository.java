package com.enrollment.market.rep;

import com.enrollment.market.entity.ShopUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShopUnitRepository extends JpaRepository<ShopUnit, UUID> {
}
