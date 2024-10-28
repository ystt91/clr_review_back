package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;

import java.util.List;

@Transactional
public interface CartService {

    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    List<CartItemListDTO> getCartItems(String email);

    List<CartItemListDTO> remove(Long cino);

}
