package hellojpa;

import com.sun.nio.sctp.PeerAddressChangeNotification.AddressChangeEvent;
import hellojpa.jpql.Address;
import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Main {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin(); // 트랜잭션 시작

		try {

			Member member = new Member();
			member.setUsername("member1");
			member.setAge(10);
			em.persist(member);

			// select 문의 프로젝션들이 다 영속성 컨텍스트에서 관리가 된다.
			// 나중에 join 문이 필요하다면 실제 SQL 문과 유사하게 코드를 작성하자 -> 명시적 조인을해야함
			List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
					.getResultList(); // 엔티티 프로젝션

			Member findMember = resultList.get(0);
			findMember.setAge(20);

			List<Team> resultList2 = em.createQuery("select m.team from Member m", Team.class)
					.getResultList(); // 엔티티 프로젝션

			List<Address> resultList3 = em.createQuery("select o.address from Order o", Address.class)
					.getResultList(); // 임베디드 타입 프로젝션

			List list = em.createQuery("select distinct m.username, m.age from Member m")
					.getResultList();// 스칼라 타입 프로젝션, 타입이 여러개일때 어떤 타입으로 반환할 수 있나

			// 오브젝트 타입으로 반환
			Object o = list.get(0);
			Object[] result = (Object[]) o;
			System.out.println("username = " + result[0]);
			System.out.println("age = " + result[1]);

			// 타입으로 반환
			List<Object[]> list2 = em.createQuery("select distinct m.username, m.age from Member m")
					.getResultList();// 스칼라 타입 프로젝션, 타입이 여러개일때 어떤 타입으로 반환할 수 있나

			Object[] result2 = list2.get(0);
			System.out.println("username = " + result2[0]);
			System.out.println("age = " + result2[1]);

			// new 해서 dto 형태로 반환
			List<MemberDTO> list3 = em.createQuery( // 단점 패키지 길어지면 다적어야해
							"select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
					.getResultList();
			MemberDTO memberDTO = list3.get(0);
			System.out.println("memberDTO = " + memberDTO.getUsername());
			System.out.println("memberDTO = " + memberDTO.getAge());

			tx.commit(); // 트랜잭션 종료
		} catch (Exception e) {
			// 뭔가 에러나 취소가 있으면 롤백
			tx.rollback();
		} finally {
			em.close();
		}
		// 마무리로 entityManger 닫아줘
		emf.close();
	}
}