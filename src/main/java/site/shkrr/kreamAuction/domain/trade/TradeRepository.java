package site.shkrr.kreamAuction.domain.trade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade,Long> {

    //사이즈별 즉시구매가 정보를 가진 거래정보를 중복되는 거래중 오래된 정보만 가져오기
    @Query(value = "select t1.* " +
            "from trade t1 " +
            "inner join (" +
            "   select product_size,MIN(product_price) as min_price " +
            "   from trade " +
            "   where status='sales_bid' AND product_id=:product_id " +
            "   group by product_size" +
            ") t2 on t1.product_size=t2.product_size AND t1.product_price=t2.min_price " +
            "where status='sales_bid' AND product_id=:product_id AND t1.created_date=(" +
            "   select MIN(created_date) " +
            "   from trade " +
            "   where product_size = t1.product_size AND product_price = t1.product_price AND status = 'sales_bid' AND product_id = :product_id" +
            ");",nativeQuery = true)
    List<Trade> getImmediatePurchaseInfo(@Param("product_id") Long productId);

    //사이즈별 즉시판매가 정보를 가진 거래정보를 중복되는 거래중 오래된 정보만 가져오기
    @Query(value = "SELECT t1.*\n" +
            "FROM trade t1\n" +
            "INNER JOIN (\n" +
            "  SELECT product_size, MAX(product_price) AS max_price\n" +
            "  FROM trade\n" +
            "  WHERE status = 'purchase_bid' AND product_id = :product_id\n" +
            "  GROUP BY product_size\n" +
            ") t2 ON t1.product_size = t2.product_size AND t1.product_price = t2.max_price\n" +
            "WHERE t1.status = 'purchase_bid' AND t1.product_id = :product_id AND t1.created_date = (\n" +
            "  SELECT MIN(created_date)\n" +
            "  FROM trade\n" +
            "  WHERE product_size = t1.product_size AND product_price = t1.product_price AND status = 'purchase_bid' AND product_id = :product_id\n" +
            ");",nativeQuery = true)
    List<Trade> getImmediateSalesInfo(@Param("product_id") Long productId);
}
