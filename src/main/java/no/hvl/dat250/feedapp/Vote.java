package no.hvl.dat250.feedapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Vote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	private Integer result;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User voter;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "poll_id")
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
