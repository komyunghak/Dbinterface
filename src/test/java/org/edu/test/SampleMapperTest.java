package org.edu.test;

import java.util.List;   // j unit 테스트    j는 자바를 의미하고, 유닛이 단위를 뜻하며 j unit test는 결국 조각조각 테스트를 한다는 의미다.
import java.util.Random;

// tip 위 아이콘을 잘보면 x와j가있다 x는 커리를 뜻하고 j는 자바를 뜻한다.
import javax.inject.Inject;

import org.edu.dao.IF_SampleDAO;
import org.edu.vo.MemberVO;
// import org.edu.dao.SampleSelectProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)//정상은 src/main/java속 들어가는게 정상이지만 이것은 테스트이기때문에 여기다가 저장을한다.
@ContextConfiguration(locations ={"file:src/main/webapp/WEB-INF/spring/**/*.xml"})//테스트용이기떄문에 경로를 바로 지정한것이지 무의미한 내용이다
public class SampleMapperTest {
	
	/**
	 * hsql 사용시 아래 설정은 무시하셔도 됩니다.
	 * 실습시 Mysql 에 아래 3가지 쿼리를 실행 하고 작업 시작 합니다.
	 * CREATE SCHEMA `interface` DEFAULT CHARACTER SET utf8 ;
	 * create table member
	(
	 userid varchar(50) not null
	    ,userpw varchar(50) not null
	    ,username varchar(50) not null
	    ,email varchar(100)
	    ,regdate timestamp default now()
	    ,updatedate timestamp default now()
	    ,primary key(userid)
	)
	* INSERT INTO member VALUES ('user2','1234','kimilguk','user02@test.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
	* 프로젝트폴더경로 DbInterface/src/main/webapp/WEB-INF/spring/root-context.xml (Mysql서버접속정보 변경해야 합니다.)
	 */
	
	// interface 로 Mybatis 쿼리 사용 DI처리(Dependency Injcetion)
	@Inject
	private IF_SampleDAO mapper; //인터페이스를 실행가능하게 mapper변수로 지정
	//클래스를 실행변수로 사용시 =>//위와같은의미 IF_SampleMapper mapper = new IF_SampleMapper(); 구형 웹프로그램에서는 이 명령어를 쓰지만 에러가 많이떠서 위에명령어로 사용한다.
	
	@Test
	public void testInsertMember() {
		int vRandom = 0;
		Random ran = new Random();
		vRandom = ran.nextInt();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		String today= formatter.format(new java.util.Date());
		testSelectMember();
		System.out.println("위쪽은 입력 전 리스트 입니다.");
		MemberVO vo = new MemberVO();
		vo.setUserid("user_" + vRandom);
		vo.setUserpw("1234");
		vo.setUsername("각시탈");
		vo.setEmail("user10@test.com");
		mapper.insertMember(vo);
		System.out.println("아래쪽은 입력 후 리스트 입니다.");
		testSelectMember();   // 이 명령어가 있으므로 자동저장이된다(워크스페이스)
	}
	@Test
	public void testSelectMember() {
		List<MemberVO> list = mapper.selectMember();
		int cnt = 1;
		for(MemberVO vo:list) {
			System.out.println(
					"번호:" + cnt++ + "번" + 
					"아이디:" + vo.getUserid() + 
					" 암호:" + vo.getUserpw() +
					" 이름:" + vo.getUsername() +
					" 이메일:" + vo.getEmail() 
					);
		}
		
	}
	@Test
	public void testUpdateMember() {	
		MemberVO vo = new MemberVO(); 
		//수정은 여러개의 변수값을 변경하기 때문에 MemberVO클래스 변수를 매개변수로 사용한다.
		testSelectMember();
		System.out.println("위에서 수정전 이름을 확인 하세요.");
		
		vo.setUserid("user12");  //유저아이디를 보내는이유는 where조건에 맞춰주기위해
		vo.setUserpw("4321");
		vo.setUsername("공주님");
		vo.setEmail("abc@abc.com");
		mapper.updateMember(vo);
		
		System.out.println("아래에서 수정 후 이름을 확인 하세요.");
		testSelectMember();
	}
		
	@Test
	public void testDeletMember() {
		testSelectMember();
		mapper.deleteMember("user11");
		System.out.println("아래는 지운 후 회원리스트 입니다.");
		testSelectMember();
	}
	
	//DB연동방식1
	@Test // 인터페이스로 사용자이름에 해당하는 아이디 검색
	public void testUserId(){
		// System.out.println(mapper.getClass().getName()); //디버그용
		String userid = mapper.getUserId("kimilguk");
		System.out.println("결과1[사용자이름에 해당하는 아이디확인] getUserId() : " + userid);
	}	
		
	// interface 로 단순쿼리 Mysql서버 시간출력.(dB연결 확인 용)
	@Test
	public void testTime(){
		// System.out.println("mapper.getClass().getName() : " + mapper.getClass().getName());
		System.out.println("결과2[db접속시간확인] getTime() : " + mapper.getTime());
	}
	
	// interface 쿼리로 Mysql서버 member테이블 에서 사용자 이름출력. 
	@Test
	public void testUname(){
		System.out.println("mapper.getClass().getName() : " + mapper.getClass().getName());
		String uname = mapper.getUname("user2", "1234");
		System.out.println("결과3[아이디/암호로 검색결과 확인] getUname() : " + uname);
	}
	
	// interface 쿼리로 Mysql서버의 member테이블 에서 사용자 ID검색 후 검색된 이름출력 단, Mapper 외부 java클래스 사용. 
	@Test
	public void testSearchUname(){
		System.out.println(mapper.getClass().getName());
		String keyword = "user2";
		String uname = mapper.searchUname("userid",keyword);
		System.out.println("결과4[mapper외부쿼리로 아이디로검색한 사용자이름확인] searchUname() : " + uname);
	}
	

}
