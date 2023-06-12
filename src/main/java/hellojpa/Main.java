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

			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Member member = new Member();
			member.setUsername("teamA");
			member.setAge(10);
			member.setTeam(team);
			member.setType(MemberType.ADMIN);
			em.persist(member);

			em.flush();
			em.clear();

			String query1 = "select m.username, 'HELLO', TRUE from Member m " +
					"where m.type = :userType";
			//쌩으로 쓰면 where m.type = hellojpa.jpql.MemberType.ADMIN 다써야함

			List<Object[]> list = em.createQuery(query1)
					.setParameter("userType", MemberType.ADMIN) // 요렇게 하는걸 권장
					.getResultList();
			// 여기서 team select 하는 쿼리는 LAZY 로 안해서 나감

			for (Object[] objects : list) {
				System.out.println("objects = " + objects[0]);
				System.out.println("objects = " + objects[1]);
				System.out.println("objects = " + objects[2]);
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