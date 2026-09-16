package com.example.shopping_cart.service;

import com.example.shopping_cart.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.dto.OrderItemResponseDTO;
import com.example.shopping_cart.dto.OrderResponseDTO;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.model.OrderItem;
import com.example.shopping_cart.model.OrderItemId;
import com.example.shopping_cart.model.Orders;
import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.repository.CartRepository;
import com.example.shopping_cart.repository.OrderItemRepository;
import com.example.shopping_cart.repository.OrdersRepository;
import com.example.shopping_cart.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdersService implements IOrdersService {

//	1. 把購物車裡的商品變成一筆「訂單」（orders）
//	2. 購物車裡每個商品變成「訂單明細」（order_item）
//	3. 每個商品的「庫存」要扣掉
//	4. 結帳完購物車要「清空」

	

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private MemberRepository memberRepository;
	

	/*
	 * 會員在不在 先檢查購物車有沒有東西 建立一筆訂單 檢查商品有沒有 商品扣庫存 把購物車商品塞進訂單明細 計算總金額 清空購物車
	 * 
	 */
	@Override
	@Transactional
	public OrderResponseDTO checkout(Integer memberID) {
//		先找有沒有會員
		Optional<Member> memberesult = memberRepository.findById(memberID);
		if (!memberesult.isPresent()) {
			throw new RuntimeException("尚未登入會員");
		}
//		拿出會員物件，訂單需要
		Member member = memberesult.get();
//		用會員找出購物車
		List<Cart> checkcart = cartRepository.findBycartIdMemberID(memberID);

		if (checkcart.isEmpty()) {
			throw new RuntimeException("購物車是空的");
		}
//		建立訂單
//		 先建訂單，金額暫時設 0 → save → 資料庫產生 orderID
//		 用 orderID 建立每筆明細，同時算總金額
//		算完之後，把總金額更新回訂單 → 再 save 一次
		Orders neworders = new Orders();
		neworders.setMember(member);
		neworders.setAmount(BigDecimal.ZERO);
		neworders.setOrderStatus("待付款");
		neworders.setOrderTime(LocalDateTime.now());
		ordersRepository.save(neworders);

//			準備計算總額
		BigDecimal totalAmount = BigDecimal.ZERO;
//			給前端看的，建立空清單，因為商品不會只有一個
		List<OrderItemResponseDTO> items = new ArrayList<>();

//		購物車的東西一個一個拿出來，檢查庫存->扣庫存->建立訂單明細->計算總價格
		for (Cart cart : checkcart) {
			Product product = cart.getProduct();

			if (product.getInventoryQuantity() < cart.getCartQuantity()) {
				throw new RuntimeException(product.getProductname() + "庫存不足");
			}
			product.setInventoryQuantity(product.getInventoryQuantity() - cart.getCartQuantity());
			productRepository.save(product);

//				 建立訂單明細，存進資料庫
			OrderItemId orderItemId = new OrderItemId(neworders.getOrderID(), product.getProductID());
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(orderItemId);
			orderItem.setOrderQuantity(cart.getCartQuantity());
			orderItem.setPurchasePrice(product.getPrice());
			orderItemRepository.save(orderItem);
//總金額
			totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getCartQuantity())));

//				給前端，建立單一盒子裝進清單
			OrderItemResponseDTO itemDTO = new OrderItemResponseDTO(product.getProductname(), cart.getCartQuantity(),
					product.getPrice());
			items.add(itemDTO);

		}
//		最後總金額加回訂單，存進資料庫
		neworders.setAmount(totalAmount);
		ordersRepository.save(neworders);
//清空購物車
		cartRepository.deleteByCartIdMemberID(memberID);

		return new OrderResponseDTO(neworders.getOrderID(), totalAmount, neworders.getOrderStatus(),
				neworders.getOrderTime(), items);

	}

}
