package hellojpa;

import hellojpa.jpql.Member;
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

			TypedQuery<Member> query1 = em.createQuery("select m from Member as m", Member.class);
			List<Member> resultList = query1.getResultList(); // 리스트로 뽑아낼 수 있음
			for (Member member1 : resultList) {
				System.out.println("member1 = " + member1);
			}

			// 값이 하나일경우
			Member singleResult = query1.getSingleResult();
			System.out.println("singleResult = " + singleResult);

			TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.username = :username", Member.class);
			query2.setParameter("username", "member1");
			Member singleResult1 = query2.getSingleResult();
			System.out.println("singleResult1 = " + singleResult1.getUsername());

			Member result = em.createQuery(
							"select m from Member m where m.username = :username", Member.class)
					.setParameter("username", "member1")
					.getSingleResult();
			System.out.println("result = " + result.getUsername());

			// 타입 정보를 받을 수 없을때
			Query query3 = em.createQuery("select m.username, m.age from Member as m");

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