package com.example.demo.repository;

import com.example.demo.entity.BpiCurrency;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BpiCurrencyRepository extends JpaRepository<BpiCurrency, BigDecimal> {

  List<BpiCurrency> findByCodeLikeOrderByCodeAsc(String code);

  BpiCurrency findByCode(String code);

  @Transactional
  void deleteByCode(String coed);

}
