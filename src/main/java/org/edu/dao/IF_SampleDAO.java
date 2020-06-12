package org.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.edu.vo.MemberVO;

/*
 *    Mapper 인터페이스는 기존의 DAO 인터페이스와 동일함.
 *    가장 큰 차이는 인터페이스의 구현을 mybatis-spring 에서 자동으로
 *    생성함.
 */
public interface IF_SampleDAO {
	// 회원정보 입력-조회-수정-삭제 메서드 명세
	public void insertMember(MemberVO vo);
	public List<MemberVO> selectMember();
	public void updateMember(MemberVO vo);
	public void deleteMember(String userid);
	
	// 인터페이스에서 dB접속 테스트
	//@Select("select current_timestamp from member") //Hsql용
	@Select("select now()") //Mysql용
	public String getTime();
	
	/*
	 *    MyBatis 어노테이션을 사용해서 두 개 이상의 파라미터에 각각
	 *    @Param 어노테이션을 붙여서 처리할 수 있음.
	 */
	//DB연동시키는방식1-스프링boot에서 주로사용: 메서드상단에 쿼리를 등록 -@Select
	@Select("select username from member "
			+ " where userid = #{uid} and userpw = #{upw}")
	public String getUname(
			@Param("uid") String uid,
			@Param("upw") String upw
	);
	
	/*
	 *    Mapper 인터페이스를 이용하는 또 다른 장점은 기존의 XML 을
	 *    그대로 사용할 수 있음.
	 *    이때 두가지 규칙이 있음.
	 *    1. Mapper 인터페이스 이름과 XML Mapper 의 네임스페이스를 반드시
	 *       일치시킬 것.
	 *    2. Mapper 인터페이스의 메서드 이름과 XML 의 id 를 반드시 일치시킴.
	 *    root-context.xml :: sqlSessionFactory 에 매퍼 xml 파일의 경로를 인식하게 함.
	 * 
	 */
	//DB연동방식2: 스프링프레임웍 많이 사용-외부에 쿼리모음을 지정.
	// 인터페이스로 사용자이름에 해당하는 아이디 검색 테스트 쿼리경로 /src/main/resources/mappers/sampleMapper.xml
	public String getUserId(
			@Param("username") String username
	);
		
	//DB연동방식3 : 쿼리를 외부파일로 지정하는데, 클래스를 사용.
	// SampleSelectProvider 쿼리경로: /src/main/java/org/edu/dao/SampleSelectProvider.java
	
	@SelectProvider(type=SampleSelectProvider.class, method="searchUname")
	public String searchUname(
			@Param("type") String type,
			@Param("keyword") String keyword
	);
	
}
