package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * JPA의 모든 데이터 변경이나 로직들은 가급적 트랜잭션 안에서 실행되어야 한다.
 * 트랜잭션은 spring과 javax에서 제공하는게 있는데 spring 쓰는게 좋다(사용할 수 있는 옵션들이 많아서)
 * public 메서드 들이 기본적으로 들어가나,
 */
/**
 * lombook 사용 시
 * @AllArgsConstructor 사용 가능
 * 생성자 injection 을 작성하지 않아도 자동으로 작성해줌
 *
 * @RequiredArgsConstructor 사용 시
 * final을 가지고 있는 필드만 가지고 생성자를 만듦
 */
@Service
@Transactional(readOnly = true)
// @AllArgsConstructor
@RequiredArgsConstructor
public class MemberService {

    /**
     * Spring에서 등록된 been을 자동으로 인젝션 해줌
     * 아래와 같이 쓸때의 단점 : 테트를 하거나 변경이 필요할 때 못 바꾼다
     * 따라서 setter injection을 사용
     */
    //@Autowired - setter injection 사용을 위해서 제거
    // 그리고 밑과 같이 작성한다면 이제 변경 할 일이 없기 때문에  final로 설정
    // 추가로 final을 설정하면 컴파일 시점에 오류 확인 가능
    public final MemberRepository memberRepository;
    /** injection 사용 예시
     * 여기를 거처서 위 변수로 이동
     * 장점 : 테스트 코드 작성 시 가짜 레파지토리 전달 가능
     * 단점 : 런타임 돌아가는 시점에 누군가가 변경이 가능
     *      -> 단 동작 시 이미 조회라던가 준비가 마무리 되기 때문에 실행 도중 누군가 호출할 가능성은 매우 낮다
     *      따라서, 이러한 상황에선 해당 방법 말고 다른 방법을 사용할 수 있다.
     *      **해당 방식이 아닌 생성자 인젝션을 사용하면 된다**
     */
    /*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
     */
    /**
     * 생성자 injection
     * 한번 생성할 때 해당 로직이 끝나(완성) 버리니깐 중간에 set을 사용하여 변수값 변경 불가능
     * 테스트 코드 작성 시 직접 주입을 해줘야 한다. (따라서 생성 시점에 어떤 것을 의존하고 있는지 명확하게 알 수 있다.)
     */
    // 최신 spring에서는 생성자가 한개면 @AutoWitred가 없어도 자동으로 해준다.
    // lombook 사용으로 작성 안해도 됨
    /*
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
     */
    /** 테스트 코드 예시 **/
    /*
    public static void main(String[] args) {
        MemberService memberService = new MemberService(Mock());
    }
    */

    /**
     * 회원 가입
     */
    /**
     * @Transactional == 기본이 false
     * 데이터 수정 부분 이므로 @Transactional(readOnly = false)로 설정
     */
    @Transactional
    public Long join(Member member) {
        validationDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 동시에 insert를 하게 된다면 오류가 발생 할 수 있기 때문에 member에 Name을 유니크 제약조건으로 잡아주는것이 좋다
     */
    private void validationDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
        //if (findMembers.size() > 1) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * @Transectional(readOnly = true)
     * 일 때 JPA에서 조회 성능을 최적화(기존 Spring과 비슷)
     * 영속성 컨텍스트에서 더티체크를 안하거나 단순 읽기 모드 등을 사용하는 등...
     */
    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 맴버 한명 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
