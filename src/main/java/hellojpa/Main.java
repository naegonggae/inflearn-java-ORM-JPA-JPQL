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

			for (int i = 0; i<100 ; i++) {
				Member member = new Member();
				member.setUsername("member"+i);
				member.setAge(i);
				em.persist(member);
			}
			// desc 내림차순
			// order by 정렬기능 디폴트 오름차순
			List<Member> result = em.createQuery("select m from Member m order by m.age desc",
							Member.class)
					.setFirstResult(1)
					.setMaxResults(10)
					.getResultList();
			System.out.println("result = " + result.size());
			for (Member member1 : result) {
				System.out.println("member1 = " + member1);

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