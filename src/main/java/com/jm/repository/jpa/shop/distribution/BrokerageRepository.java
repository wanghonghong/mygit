package com.jm.repository.jpa.shop.distribution;

import com.jm.repository.po.shop.brokerage.Brokerage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrokerageRepository extends JpaRepository<Brokerage, Integer>{


    Brokerage findByUserId(int userId);
}
