# DS-META - Desafio consulta vendas

Uma simples aplicação de vendas e vendedores que exibe o relatorio de vendas e o sumario do total de vendas do vendedor!

---
## OBJETIVO CENTRAL DA APLICAÇÃO

Essa aplicação não tem um objetivo central de foco, exatamente, foi mais projetada para estudar JPQL, SPRING DATA JPA E QueryMethods em relação **@MANY-TO-ONE**.

## DESAFIO

O principal desafio foi implementar consultas otimizadas com Query methods e JOIN FETCH, cubrindo o problema do N+1 em relações de entidades.

### Resultados elaborados

#### GET /sales/report
Aqui implementados uma consulta complexa, para retornar paginado o total de vendas, filtrado por datas e pelo vendedor.
Foi utilizado a clausula JOIN FETCH para otimizar a consulta, ou seja retornar tudo em apenas uma consulta, evitando o N+1.
Foi implementado o countQuery para que a paginação com o JOIN FETCH funcionasse, ja que ele retorna o total de paginas, necessarios para paginação.
```
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
```

#### GET /sales/summary
Aqui implementados uma consulta complexa, para retornar o total de faturamento, filtrado por datas dos vendedores.
Foi utilizado a tecnica de projeção de dados para otimizar a consulta, ou seja retornar apenas os dados necessarios.
```
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
```

