package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_name")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item", // 관계형 DB는 객체처럼 설계가 힘들기 떄문에 1:N, N:1로 풀어내는 중간 테이블이 필요
            joinColumns = @JoinColumn(name = "category_id"), // 카테고리에서 나가는 ID
            inverseJoinColumns = @JoinColumn(name = "item_id")) // 카테고리에서 외부로 들어가는 ID
    private List<Item> items = new ArrayList<>();

    /** 카테고리 구조 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 셀프로 양방향 연관관계를 형성한 것
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //*** 연관관계 (편의) 메서드 ***//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}
