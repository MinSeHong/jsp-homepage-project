package service;

import java.util.List;
import java.util.Map;

//제너릭 인터페이스
public interface DaoService<T> {

	void close();
	List<T> boardRecords(Map page);
	T boardRecordOne(String no);
	int writeOnBoard(String title, String content, String id);
	int updateOnBoard(String title, String content,String no);
	int deleteOnBoard(String no);
	int getTotalRecordCount(Map map);
}
