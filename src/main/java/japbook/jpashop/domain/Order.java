package japbook.jpashop.domain;

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
    public void getOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
