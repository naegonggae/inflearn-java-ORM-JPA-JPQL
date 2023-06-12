package hellojpa.jpql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Member {
	@Id @GeneratedValue
	private Long id;
	private String username;
	private int age;

	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;

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

	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", username='" + username + '\'' +
				", age=" + age +
				'}'; // toString 할때 team 은 지우는게 좋다. 만약 Member 와 Team 모두 toString 하면 무한 루프를 탄다.
	}
}
