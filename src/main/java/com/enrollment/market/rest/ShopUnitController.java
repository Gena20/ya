package com.enrollment.market.rest;

import com.enrollment.market.dto.InputImport;
import com.enrollment.market.dto.InputShopUnit;
import com.enrollment.market.entity.ShopUnit;
import com.enrollment.market.entity.ShopUnitType;
import com.enrollment.market.rep.ShopUnitRepository;
import exception.ShopUnitNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class ShopUnitController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopUnitController.class);

    private final ShopUnitRepository repository;
    private final EntityManager em;

    ShopUnitController(ShopUnitRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }
    @RequestMapping("/all")
    public Iterable<ShopUnit> all() {
        LOGGER.info("Hello, Log!");

        return repository.findAll();
    }

    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.OK)
    public void imports(@RequestBody InputImport input) {
        List<InputShopUnit> inputUnits = input.getItems();
        List<ShopUnit> units = new ArrayList<>();
        Set<ShopUnit> categories = new HashSet<>();

        for (InputShopUnit inputUnit : inputUnits) {
            ShopUnit unit = makeShopUnit(inputUnit, input.getUpdateDate());

            repository.saveAndFlush(unit);
            units.add(unit);
        }

        for (ShopUnit unit : units) {
            updateParentDate(unit, input.getUpdateDate());
            updateChildrenDate(unit, input.getUpdateDate());

            if (unit.getType() == ShopUnitType.CATEGORY) {
                categories.add(unit);
            }

            ShopUnit parent = unit.getParent();
            if (parent != null) {
                categories.add(parent);
            }
        }

        categories.forEach(this::updateParentPrice);

        repository.flush();
    }

    private ShopUnit makeShopUnit(InputShopUnit inputUnit, Timestamp date) {
        ShopUnit unit = repository.findById(inputUnit.getId()).orElse(new ShopUnit());

        unit.setId(inputUnit.getId());
        unit.setName(inputUnit.getName());
        unit.setType(inputUnit.getType());
        unit.setDate(date);

        UUID parentId = inputUnit.getParentId();
        if (parentId != null) {
            repository.findById(parentId).ifPresent(unit::setParent);
        }

        if (inputUnit.getType() == ShopUnitType.OFFER) {
            unit.setPrice(inputUnit.getPrice());
        }

        return unit;
    }

    private void updateParentDate(ShopUnit unit, Timestamp date) {
        while (unit.getParent() != null) {
            unit.getParent().setDate(date);
            unit = unit.getParent();
        }
    }

    private void updateChildrenDate(ShopUnit unit, Timestamp date) {
        if (unit.getChildren() == null) {
            return;
        }

        for (ShopUnit childUnit : unit.getChildren()) {
            childUnit.setDate(date);
            updateChildrenDate(childUnit, date);
        }
    }

    private void updateParentPrice(ShopUnit unit) {
        while (unit != null && unit.getChildren() != null) {
            Long sum = null;
            Long counter = 0L;

            for (ShopUnit childUnit : unit.getChildren()) {
                LOGGER.info(childUnit.getName());
                Long price = childUnit.getPrice();
                if (price == null) {
                    continue;
                }

                if (sum == null) {
                    sum = 0L;
                }

                sum += price;
                counter++;
            }

            if (sum != null && counter != 0) {
                LOGGER.info(String.valueOf(unit.getPrice()));
                unit.setPrice(sum / counter);
                LOGGER.info(String.valueOf(unit.getPrice()));
                repository.flush();
            }

            unit = unit.getParent();
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        ShopUnit unit = repository.findById(id).orElseThrow(ShopUnitNotFoundException::new);

        repository.delete(unit);
    }

    @GetMapping("/nodes/{id}")
    public ShopUnit nodes(@PathVariable UUID id) {
        return repository.findById(id).orElseThrow(ShopUnitNotFoundException::new);
    }
}
