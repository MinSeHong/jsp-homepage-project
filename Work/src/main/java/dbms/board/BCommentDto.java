package dbms.board;

import java.sql.Date;

public class BCommentDto {
	private String bno;
	private String cno;
	private String bcomment;
	private Date postdate;
	private String id;
	

	private String name;
	private String postrank;
	
	public BCommentDto() {}


	public String getBno() {
		return bno;
	}

	public void setBno(String bno) {
		this.bno = bno;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getBcomment() {
		return bcomment;
	}

	public void setBcomment(String bcomment) {
		this.bcomment = bcomment;
	}

	public Date getPostdate() {
		return postdate;
	}

	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPostrank() {
		return postrank;
	}


	public void setPostrank(String postrank) {
		this.postrank = postrank;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	
	
}
