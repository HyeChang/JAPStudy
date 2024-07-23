package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberID, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberID);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성(회원에 있는 정보를 넣어줌)
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 여기선 간단히 회원의 Address를 읽어와 저장해줌

        /*
        유지보수(관리)를 용이하게 하기 위해서 생성로직 분산을 막기위하여 다른 스타일의 생성을 막는게 좋다
        방법은? 기본 생성자를 protected로 선언해 둔다. (ex. protected OrderItem{})
        위 코드를 lombok을 이용해 간단히 줄일 수 있다.  @NoArgsConstructor(access = AccessLevel.PROTECTED 를 선언
         */
        // 주문상품 생성(생성 메서드 호출)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성(생성 메서드 호출)
        Order order = Order.createOrder(member, delivery, orderItem);

        /*
        원래는 delivery, OrderItem을 따로 생성(persist) (각 Repository가 존재해 set을 통해서 저장해야 하는데)
        밑의 코드 한 줄로 저장이 가능하다.
        이유는, orderItems, delivery 에 지정한 "cascade = CascadeType.ALL" 옵션에 의하여 위 과정이 생략 된것
        cascade 란? Order에 persist가 수행되면 해당 옵션이 걸린 코드도 함께 수행된다.
        언제 사용할까? 참조가 private 하거나(1:1 등등) 할 때, (개발 진행하며 감이 왔을 때 리팩토링하여 사용하는걸 권고)
        중효한 부분이고, 여러 곳에서 사용할 때는 해당 옵션을 사용하면 관리 및 사용이 힘들다.
         */
        // 주문 저장
        orderRepository.save(order);
        
        return order.getId(); // Order의 식별자 값 반환
    }


    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderID) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderID);
        /*
        각 비즈니스 로직에서 cancel 로직을 수행하기 떄문에 아래와 같이 취소 코드를 간단하게 작성할 수 있다.
        - 도메인 모델 패턴 : 단순 조회 로직을 제외하고 비즈니스 로직이 대부분 엔티티에 있는 스타일
        - 트랜젝션 스크립트 패턴 : 반대로 엔티티에는 비즈니스 로직이 거의 없고, 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것
         */
        // 주문 취소
        order.cancel();
    }
    
    // 검색
    
}
