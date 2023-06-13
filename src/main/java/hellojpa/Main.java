package hellojpa;

import com.sun.nio.sctp.PeerAddressChangeNotification.AddressChangeEvent;
import hellojpa.jpql.Address;
import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.MemberType;
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

			Member member1 = new Member();
			member1.setUsername("관리자1");
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("관리자2");
			em.persist(member2);

			em.flush();
			em.clear();
			String query1 = "select m.username from Member m"; // 상태 필드 경로탐색의 끝
			String query2 = "select m.team from Member m"; // 묵시적 내부 조인이 일어남 = 단일값 연관 경로
			// 참고로 묵시적 내부 조인이 일어나게 쿼리를 짜면 안된다. 쿼리 튜닝할때 힘들어짐
			String query3 = "select t.members from Team t"; // 컬렉션 값 연관경로, 묵시적 내부 조인 발생, 컬렉션이기때문에 더이상 탐색 불가
			List<Member> result = em.createQuery(query3, Member.class)
					.getResultList();

			for (Member s : result) {
				System.out.println("s = " + s);
			}

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