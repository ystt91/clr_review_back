package org.zerock.mallapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.dto.CartItemListDTO;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 특정한 사용자의 모든 장바구니 아이템들을 가져온다.
    // input -> email / output -> CartItemListDTO

    //Long cino, int qty, String pname, int price, String imageFile
    @Query("select " +
            "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price,p.pno, pi.fileName) " +
            "from CartItem ci inner join Cart c on ci.cart = c " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where c.owner.email = :email and pi.ord = 0 " +
            " order by ci.cino desc")
    List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    // 이메일, 상품 번호로 해당 상품이 장바구니 아이템으로 존재하는지 확인
    @Query("select ci from CartItem ci left join Cart c on ci.cart = c " +
            "where c.owner.email = :email and ci.product.pno = :pno")
    CartItem getItemOfPno(@Param("email")String email, @Param("pno") Long pno);

    // 장바구니 아이템 번호로 장바구니를 얻어오려고 하는 경우
    @Query("select c.cno from Cart c left join CartItem ci on ci.cart = c where ci.cino = :cino")
    Long getCartFromItem(@Param("cino") Long cino);

    // 장바구니 번호로 모든 장바구니 아이템들 조회
    @Query("select " +
            "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price,p.pno, pi.fileName) " +
            "from CartItem ci inner join Cart c on ci.cart = c " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where pi.ord = 0 and c.cno = :cno " +
            "order by ci.cino desc")
    List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);

}
