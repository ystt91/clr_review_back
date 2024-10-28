package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct(){
        String email = "user1@aaa.com";
        Long pno = 5L;
        int qty = 1;

        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem != null){
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
        }

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        if(result.isEmpty()){
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        }else{ //cart는 있으나 상품의 장바구니 아이템은 없을 경우
            cart = result.get();
        }

        if(cartItem == null){
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().cart(cart).product(product).qty(qty).build();
        }

        cartItemRepository.save(cartItem);

    }

    @Test
    public void testListOfMember(){
        String email = "user1@aaa.com";

        cartItemRepository.getItemsOfCartDTOByEmail(email);
    }
}
