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
			String query2 = "select m from Member m join fetch m.team";
			// 이렇게하면 Team 은 프록시로 담기지 않고 실제 데이터로 담겨온다.
			// join 앞에 left 붙이면 outer 조인
			// 실무에서 엄청 쓴다고 함
			List<Member> result = em.createQuery(query2, Member.class)
					.getResultList();

			for (Member member : result) {
				System.out.println("member = " + member.getUsername() +", "+ member.getTeam().getName());
				// 회원1, 팀A(SQL)
				// 회원2, 팀A(1차 캐시)
				// 회원3, 팀B(SQL)
				// select 문이 최초 member 하나, 팀A 가져올때 하나, 팀B 가져올때 하나 해서 세번 출력됐다.
				// 만약 회원 100명이라면 -> N + 1
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