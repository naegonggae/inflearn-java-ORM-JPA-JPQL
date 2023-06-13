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
			String query1 = "select m from Member m";
			// 여기는 Team 이 프록시로 담겨서 온다.
			String query2 = "select distinct t from Team t join fetch t.members";
			// distinct 는 중복되는 엔티티를 제거 해줌 -> 데이터 뻥튀기를 안하고 싶다하면 사용
			// 이렇게하면 Team 은 프록시로 담기지 않고 실제 데이터로 담겨온다.
			// join 앞에 left 붙이면 outer 조인
			// 실무에서 엄청 쓴다고 함
			String query3 = "select t from Team t join t.members m";
			// 초기에 팀정보만 불러옴 그다음 멤버 정보 불러오고 함
			// 일반조인과 패치 조인의 차이
			List<Team> result = em.createQuery(query3, Team.class)
					.getResultList();

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