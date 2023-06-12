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
			member.setUsername("관리자");
			member.setAge(10);
			member.setTeam(team);
			member.setType(MemberType.ADMIN);
			em.persist(member);

			em.flush();
			em.clear();

//			String query1 = "select concat('a', 'b') from Member m";
			String query1 = "select 'a' || 'b' from Member m"; // 하이버네이트에서 지원
			String query2 = "select substring(m.username, 2, 3) from Member m";
			String query3 = "select locate('de', 'abcdefg') from Member m"; // 'de' 가 있는지 있으면 인덱스 출력
			String query4 = "select size(t.members) From Team t";
			List<Integer> result = em.createQuery(query4, Integer.class)
					.getResultList();

			for (Integer s : result) {
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