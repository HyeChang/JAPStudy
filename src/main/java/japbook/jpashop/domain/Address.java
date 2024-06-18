package japbook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
/** 좋은 설계는 생성할때만 값이 변경되어야 함(Getter 설정을 하면 안됨)
 * 기본 생성자를 만들어줘야 함 = JPA 기본 스펙에서 리플랙션이랑 프록시를 사용해야 하는데 기본생성자가 없으면 사용하지 못하기 떄문에 기본 생성자를 생성해줘야 함
 * 생성자에서 값을 모두 초기화해서 변경 불가능ㅎ란 클래스를 만들어야 함.
 **/
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // public 이면 외부에서 호출이 가능하므로 protected 사용(JPA에서 여기까지 허용 해줌)
    protected Address() {
    }

    // 단축키 : alt + shift + s => Generate Constructor using Fields => 선택
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
