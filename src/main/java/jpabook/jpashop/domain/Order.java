package jpabook.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Setter @Getter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Cascade 사용하는 이유
    /**
     * 원래 각자 해줘야 하나 해당 옵션으로 자동으로 해줌
     *
     * 사용 X
     * persist(orderItemA)
     * persist(orderItemB)
     * persist(orderItemC)
     * persist(order)
     *
     * 사용 O
     * persist(order)
     *
     * order 호출 시 모두 전파해서 같이 persist 함
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 : [ORDER, CANCEL]

    //*** 연관관계 (편의) 메서드 ***//
    /**
     * 서로 연관된 테이블에 데이터 생성 시 하나의 테이블이 아닌 연관된 모든 테이블에 데이터가 같이 들어가야 함으로 설정 필요
     *
     * 해당 메서드가 없을 때 코드
     *
     * public static void main(String[] args) {
     * 	Member member = new Member();
     * 	Order order = new Order();
     *
     * 	member.getOrders().add(order);
     * 	order.setMember(member);
     * }
     *
     */
    // member = manyToOne
    // 주체가 order 앤티티기 떄문에 set 가능
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    // orderItem = oneToMany
    // 주체가 orderItem 이기 떄문에 set 불가능 따라서 get만 가능
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    // 복잡한 생성을 생성 메서드가 있으면 좋다
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        // COMP == 배송완료
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        // 루프를 돌면서 재고를 원상복귀 (주문이 2개 생성 = 각각의 주문을 cancel
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        // orderItem에서 (주문 가격 * 주문 수량) 의 값을 가지고와서 합
        // 람다 or 스트림으로 더욱 깔끔한 코드로 변경 가능 Alt + Enter 로 변경(확인) 가능
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
