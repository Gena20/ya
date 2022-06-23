package com.enrollment.market;

import com.enrollment.market.entity.ShopUnit;
import com.enrollment.market.entity.ShopUnitType;
import com.enrollment.market.rep.ShopUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.Instant;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ShopUnitRepository repository) {
//        ShopUnit parent = new ShopUnit();
//        parent.setName("Parent");
//        parent.setType(ShopUnitType.CATEGORY);
//        parent.setDate(Timestamp.from(Instant.now()));
//
//        ShopUnit q = new ShopUnit();
//        q.setDate(Timestamp.from(Instant.now()));
//        q.setName("s");
//        q.setType(ShopUnitType.OFFER);
//        q.setParent(parent);
//
//        return args -> {
//            log.info("Preloading " + repository.save(q));
//        };

        return args -> {};
    }
}