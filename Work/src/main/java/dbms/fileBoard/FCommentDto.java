package dbms.fileBoard;

import java.sql.Date;

public class FCommentDto {
	private String fno;
	private String fcno;
	private String fcomment;
	private Date postdate;
	private String id;

	private String name;
	private String postrank;
	
	
	public String getFno() {
		return fno;
	}
	public void setFno(String fno) {
		this.fno = fno;
	}
	public String getFcno() {
		return fcno;
	}
	public void setFcno(String fcno) {
		this.fcno = fcno;
	}
	public String getFcomment() {
		return fcomment;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	public Date getPostdate() {
		return postdate;
	}
	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
}
