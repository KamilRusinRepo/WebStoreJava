package com.shop.prshop.repository;

import com.shop.prshop.model.Item;
import org.hibernate.query.spi.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.make = ?1 ORDER BY i.id")
    List<Item> findByMake(String make);

    @Query("SELECT i FROM Item i WHERE i.type = ?1 ORDER BY i.id")
    List<Item> findByCategory(String category);

    @Query("SELECT i FROM Item i WHERE i.make = ?1 ORDER BY i.id")
    List<Item> findTop6ByItemMake(String make);

    @Query("SELECT i FROM Item i WHERE i.make = ?1 ORDER BY CASE WHEN ?2 = 'fullName' THEN i.fullName " +
            "WHEN ?2 = 'type' THEN i.type END DESC")
    List<Item> findByMakeAndOrderBy(String make, String sort);

    @Query("SELECT i FROM Item i WHERE i.make = ?1 ORDER BY i.price")
    List<Item> findByMakeAndOrderByPrice(String make);

    @Query("SELECT i FROM Item i WHERE i.type = ?1 ORDER BY i.price")
    List<Item> findByCategoryAndOrderByPrice(String category);

    @Query("SELECT i FROM Item i WHERE i.type = ?1 ORDER BY CASE WHEN ?2 = 'fullName' THEN i.fullName " +
            "WHEN ?2 = 'brand' THEN i.make END ASC ")
    List<Item> findByCategoryOrderBy(String category, String sort);
}
