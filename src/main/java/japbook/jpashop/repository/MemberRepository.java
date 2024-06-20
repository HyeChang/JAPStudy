package japbook.jpashop.repository;


import jakarta.persistence.EntityManager;
import japbook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.PersistenceContext;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // JPA 표준 어노테이션
    // EntityManager를 만들어서 밑 변수에다 주입
    /**
     * 원래 EntityManager @PersistenceContext 라는 표준 어노테이션이 있어야 인젝션이 가능하나
     * Spring boot JPA를 사용하면 @PersistenceContext 어노테이션을 지우고 @AutoWrite 사용 가능
     * @Autowired를 사용하여 생성자 생각하는 방식 사용 가능
     */
    // @PersistenceContext - Spring boot JPA에서 @Autowirte로 사용 가능
    //private EntityManager em;
    @Autowired
    private final EntityManager em;
    // @RequiredArgsConstructor 사용으로 생략된 코드
    /*
    public MemberRepository(EntityManager em) {
        this.em = em;
    }
     */

    // JPA에서 member를 받아서 저장
    // persist에서 바로 DB에 저장하는 것이 아니라 영속성 컨텍스트레 우선 저장(영속화)
    public void save(Member member) {
        em.persist(member);
    }

    // 단건 조회
    public Member findOne(Long id) {
        // 참고용으로 밑의 코드 추가
        //원래 코드 : return em.find(Member.Class, id);
        Member member = em.find(Member.class, id);
        return member;
    }

    public List<Member> findAll() {
        // 두번째 인자 = 반환 타입
        // 이해하기 편해라고 result와 분리 해놓은 상태
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        // Ctrl + Alt + N = 바로 합쳐 짐(인라인 변수)
        return result;
    }

    // 멤버에서 이름으로 검색
    public List<Member> findByName(String name) {
        // :name = .setParameter를 통해 변수를 바인딩 하는 것.
        return em.createQuery("select m from Member m where :name", Member.class)
                .setParameter("name", name)
                .getResultList();

    }

}
