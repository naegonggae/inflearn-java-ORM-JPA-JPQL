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

			String query1 = "select " +
								  "case when m.age <= 10 then '학생요금' "+
								  "      when m.age >= 60 then '경로요금' "+
								  "      else '일반요금' " +
							 	  "end " +
					       "from Member m";
			// 조건문
			String query2 = "select coalesce(m.username, '아름 없는 회원') from Member m";
			// 멤버 이름이 null 이면 이름 없는 회원으로 반환
			String query3 = "select nullif(m.username, '관리자') from Member m";
			// 사용자 이름이 '관리자' 이면 null 반환
			List<String> result = em.createQuery(query3, String.class)
					.getResultList();

			for (String s : result) {
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