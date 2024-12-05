package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SellerDTO;
import com.devsuperior.dsmeta.entities.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

    public Page<SaleDTO> report(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate endDate = maxDate == null || maxDate.equals("") ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()) : LocalDate.parse(maxDate);
		LocalDate startDate = minDate == null || minDate.equals("") ? endDate.minusYears(1L) : LocalDate.parse(minDate);
		String sellerName = name == null || name.equals("") ? null : name;
		Page<Sale> salePage = this.repository.report(startDate, endDate, sellerName, pageable);
		return salePage.map(sale -> new SaleDTO(
				sale.getId(),
				sale.getDate(),
				sale.getAmount(),
				sale.getSeller().getName()
		));
    }

	public List<SellerDTO> summary(String minDate, String maxDate) {
		LocalDate endDate = maxDate == null || maxDate.equals("") ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()) : LocalDate.parse(maxDate);
		LocalDate startDate = minDate == null || minDate.equals("") ? endDate.minusYears(1L) : LocalDate.parse(minDate);
		return this.repository.summary(startDate, endDate);
	}

}
