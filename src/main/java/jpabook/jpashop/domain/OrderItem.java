package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED) // 생성 방식을 생성 메서드 호출로 제한한다.(다른 방식으로 사용 못하게)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "orger_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //==생성 메서드==//
    // order에서 createOrder 생성시 넘어오는(처리된?) 값들을 받아서 처리
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        // item.orderPrice 안하는 이유 => 쿠폰 적용 및 기타 할인 상황 발생 가능성 있음
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        
        // 주문이 들어갔으니 재고 감소
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        // 주문이 취소 되었으므로 재고를 주문 수량만큼 늘려줘야 함.
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}

