package dbms.board;

import java.sql.Date;

public class BoardDto {
	private String bno;
	private String title;
	private Date postdate;
	private String hitcount;
	private String likecount;
	private String content;
	private String id;
	
	private String name;
	private String postrank;
	private String commentcount;
	
	
	///생성자
	public BoardDto() {}
	public BoardDto(String bno, String title, Date postdate, String hitcount, String content, String id) {
		super();
		this.bno = bno;
		this.title = title;
		this.postdate = postdate;
		this.hitcount = hitcount;
		this.content = content;
		this.id = id;
	}


	public String getBno() {
		return bno;
	}
	public void setBno(String bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getPostdate() {
		return postdate;
	}
	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}
	public String getHitcount() {
		return hitcount;
	}
	public void setHitcount(String hitcount) {
		this.hitcount = hitcount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLikecount() {
		return likecount;
	}
	public void setLikecount(String likecount) {
		this.likecount = likecount;
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
	public String getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	
	
	
}
