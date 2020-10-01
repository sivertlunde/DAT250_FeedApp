package no.hvl.dat250.feedapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Entity
public class Vote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	private Integer result;
	
	@ManyToOne
	@JoinTable(
			name = "user_votes",
			joinColumns = @JoinColumn(name = "vote_fk"),
			inverseJoinColumns = @JoinColumn(name = "user_fk"))
	private User voter;
	
	@ManyToOne
	@JoinTable(
			name = "poll_votes",
			joinColumns = @JoinColumn(name = "vote_fk"),
			inverseJoinColumns = @JoinColumn(name = "poll_fk"))
	private Poll poll;
	
	public Vote() {}

	public Vote(Integer result, User voter, Poll poll) {
		super();
		this.result = result;
		this.voter = voter;
		this.poll = poll;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public User getVoter() {
		return voter;
	}

	public void setVoter(User voter) {
		this.voter = voter;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	@Override
	public String toString() {
		return "Vote [id=" + id + ", result=" + result + "]";
	}

}
