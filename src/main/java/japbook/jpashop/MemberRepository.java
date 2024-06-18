package japbook.jpashop;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    // spring boot에서 "@PersistenceContext" 어노테이션 사용 시 application.yml 파일 등 설정을 들고와 Entity Manager을 생성
    @PersistenceContext
    private EntityManager em;

    // 커맨드랑 쿼리를 분리 하는 스타일(리턴값을 안 만듦), 최소한으로 ID값만 반환함으로서 추 후 ID깞으로 동작 실행
//	public Long save(Member member) {
//		em.persist(member);
//		return member.getId();
//	}
//
//	public Member find(Long id) {
//		return em.find(Member.class, id);
//	}

}

