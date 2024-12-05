package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SellerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    /**
     * Para utilizar a estrategia de paginação com JOIN FETCH em relação @ManyToOne precisamos utilizar o countQuery e o resultado deve retornar a entidade diretamente
     * Essa estrategia não funciona para projeção de dados, ou seja se tentar retornar um DTO vai dar pau!
     * @param startDate
     * @param endDate
     * @param sellerName
     * @param pageable
     * @return Page<Sale></>
     */
    @Query(
            value = """
                SELECT s
                FROM Sale s
                JOIN FETCH s.seller
                WHERE s.date BETWEEN :startDate AND :endDate
                AND (:sellerName IS NULL OR LOWER(s.seller.name) LIKE LOWER(CONCAT('%', :sellerName, '%')))
            """,
            countQuery = """
                SELECT COUNT(s)
                FROM Sale s
                JOIN s.seller
            """
    )
    Page<Sale> report(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sellerName") String sellerName,
            Pageable pageable
    );

    /**
     * AO utilizar projeção de dados, com o JPQL, não tem necessidade de utilizar o JOIN FETCH, ja que aqui estamos limitando os dados, ou seja o sql so vai retornar os dados necessarios projetados.
     * @param startDate
     * @param endDate
     * @return List<SellerDTO></>
     */
    @Query(
            value = """
                SELECT new com.devsuperior.dsmeta.dto.SellerDTO(s.seller.name, SUM(s.amount)) 
                FROM Sale AS s 
                WHERE s.date BETWEEN :startDate AND :endDate
                GROUP BY s.seller.id
                ORDER BY s.seller.name
            """
    )
    List<SellerDTO> summary(LocalDate startDate, LocalDate endDate);
}
