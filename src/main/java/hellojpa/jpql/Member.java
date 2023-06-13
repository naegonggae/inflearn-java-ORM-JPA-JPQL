package hellojpa.jpql;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(
		name = "Member.findByUsername",
		query = "select m from Member m where m.username =:username"
		// 오류검증을 해준다.
		// 실무에서 이렇게 쓰진않아 대신 jpa 사용하고 레파지토리에 @Query 어노테이션 사용함
)
public class Member {
	@Id @GeneratedValue
	private Long id;
	private String username;
	private int age;

	@Enumerated(EnumType.STRING)
	private MemberType type;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public MemberType getType() {
		return type;
	}

	public void setType(MemberType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", username='" + username + '\'' +
				", age=" + age +
				'}'; // toString 할때 team 은 지우는게 좋다. 만약 Member 와 Team 모두 toString 하면 무한 루프를 탄다.
	}
}
