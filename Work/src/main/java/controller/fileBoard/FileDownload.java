package controller.fileBoard;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FileUtils;

@WebServlet("/download")
public class FileDownload extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//파라미터 받기
		String filename = req.getParameter("filename");
		//다운로드와 관련된 모델 호출]
		//1.파일 다운로드 로직 호출
		FileUtils.download(filename, "/upload/fileBoard", req, resp);
		//2.테이블의 다운로드수 컬럼 증가용 데이타베이스 관련 로직 호출
	}
}
