package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.service.MemberService;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JPA 동작을 확인 해야 하기 때문에 DB 동작까지 테스트
 * @RunWith : Junit5 버전 부터 생략 가능
 * @SpringBootTest : 해당 어노테이션이 있어야 Spring boot 인티그레이션(통합) 가능
 */
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em; // insert문 보기 위해서

    /**
     * JPA에서 같은 Transection에서 같은 Entity일 때 PK(ID)값이 같으면
     * 같은 영속성 컨텍스트에서 하로만 관리가 됨.
     * 실행 시 insert 문이 나오는 이유는 '@Transactional' 어노테이션이 roll Back을 시키기 때문
     */
    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception {
        //given
        // 1.이렇게 넘겼을 때
        Member member = new Member();
        member.setName("Hong");

        //when
        // 2.이렇게 하면
        Long saveId = memberService.join(member);

        //then
        // 3.이렇게 된다
        // assert : 코드 타당성 검증할 때 사용
        // 받은 값을 flush 시켜 영속성 컨텍스트에서 강제로 반영
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    /**
     * 예외 발생하는 것을 try catch문으로 잡아서 확인
     * ㄴ spring boot에서는 어노테이션으로 코드 감소 가능
     * JUnit5에서는 'expected' assertThrows 메서드를 사용하여 예상되는 예외의 유형과 람다 식을 통해 테스트 중인 코드를 전달
     */
    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("Hong");

        Member member2 = new Member();
        member2.setName("Hong");

        //when
        memberService.join(member1);
        // Junit5 : 예외 발생 시 정상적으로 테스트를 빠져나오기 때문에 fail() 함수도 지워야 함
        /*
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
         */
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));


        // 어노테이션 사용 전 코드
        /*
        try {
            memberService.join(member1);
        } catch (IllegalCallerException e) {
            // 테스트기 때문에 return이 되어야 테스트 성공
            return;
        }
        */

        //then
        // assert 의 fall()
        //fail("예외가 발생해야 함");
    }

}

