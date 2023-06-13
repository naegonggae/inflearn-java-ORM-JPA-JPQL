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

			Team teamA = new Team();
			teamA.setName("teamA");
			em.persist(teamA);

			Team teamB = new Team();
			teamB.setName("teamB");
			em.persist(teamB);

			Member member1 = new Member();
			member1.setUsername("회원1");
			member1.setTeam(teamA);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("회원2");
			member2.setTeam(teamA);
			em.persist(member2);

			Member member3 = new Member();
			member3.setUsername("회원3");
			member3.setTeam(teamB);
			em.persist(member3);

			em.flush();
			em.clear();
			String query1 = "select distinct t from Team t join fetch t.members";
			// 패치조인은 별칭을 줄 수 없다. -> join fetch t.members m 이렇게
			String query2 = "select t from Team t";
			// N+1 문제를 패치조인이나 배치사이즈를 통해 해결가능하다.
			// 배치 사이즈하면 원래 team을 조회하고 멤버 수만큼 멤버를 계속 조회하는데 멤버를 조회할때 아이디 값을 다 가져와서 한번에 조회할수있다.

			List<Team> result = em.createQuery(query2, Team.class)
					.setFirstResult(0)
					.setMaxResults(2)
					.getResultList();

			System.out.println("result = " + result.size());

			for (Team team : result) {
				System.out.println("team = " + team.getName() +", "+ team.getMembers().size());
				// 데이터 뻥튀기 된다. 일대다가 그럼 / 다대일은 뻥튀기 걱정없음
				// 팀은 A랑 B 이렇게 두개밖에 없지만 팀A에 해당하는 멤버가 두명이기때문에 db에 들어갈때는
				// 팀A-회원1 / 팀A-회원2 / 팀B-회원3 해서 3개가 된다는 말이다.
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