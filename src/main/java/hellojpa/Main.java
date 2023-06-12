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

			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Member member = new Member();
			member.setUsername("teamA");
			member.setAge(10);
			member.setTeam(team);
			em.persist(member);

			em.flush();
			em.clear();

			// inner join 에서 inner 생략가능
			String query1 = "select m from Member m join m.team t";
			// left outer join 에서 outer 생략가능
			String query2 = "select m from Member m left join m.team t";
			// 세타 조인
			String query3 = "select m from Member m, Team t where m.username = t.name";
			// 조인 대상 필터링
			String query4 = "select m from Member m left join m.team t on t.name = 'teamA'";
			// 연관관계 없는 엔티티 외부 조인
			String query5 = "select m from Member m left join Team t on m.username = t.name";

			List<Member> result = em.createQuery(query5, Member.class)
					.getResultList();
			// 여기서 team select 하는 쿼리는 LAZY 로 안해서 나감

			System.out.println("result = " + result.size());

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