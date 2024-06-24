package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 상품 저장
     * Item은 처음 저장할 때 ID 값이 없다(새로 생성하는 객체). 따라서 persist에 저장
     * em.merge = update와 비슷하나 자세한 개념은 나중에
     */
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    /**
     * 상품 하나 검색
     */
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 여러 상품 검색
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
