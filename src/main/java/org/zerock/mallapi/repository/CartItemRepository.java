package org.zerock.mallapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.dto.CartItemListDTO;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 특정한 사용자의 모든 장바구니 아이템들을 가져온다.
    // input -> email / output -> CartItemListDTO

    //Long cino, int qty, String pname, int price, String imageFile
    @Query("select " +
            "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            "from " +
            "CartItem ci inner join Cart mc on ci.cart = mc " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where " +
            "mc.owner.email = :email and pi.ord = 0 order by ci.cino desc")
    List<Object[]> getItemsOfCartDTOByEmail(@Param("email") String email);

    // 이메일, 상품 번호로 해당 상품이 장바구니 아이템으로 존재하는지 확인이 필요합니다.


    // 장바구니 아이템 번호로 장바구니를 얻어오려고 하는 경우


    // 장바구니 번호로 모든 장바구니 아이템들 조회

}
