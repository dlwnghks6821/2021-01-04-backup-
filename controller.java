package com.exhibition.project;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exhibition.project.BoardDao.BoardDao;

import com.exhibition.project.BoardDto.BoardDto;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BoardController {
	@Autowired
	private SqlSession sqlSession;
	private String Userid;
	private String Rankingid;
	
	// ==> �Խ��� �� �о���� <==//
	@RequestMapping("/new12")
	public String new12() {
		return "new12";
	}
	//�Խ����� �ߴ���� ..?//
	@RequestMapping(value = "/IfBoard", method = RequestMethod.POST)
	public String IfBoard(HttpServletRequest req, Model model) {
		String userid = req.getParameter("userid");
		Userid=userid;
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		int checkCount = dao.memberCheck(userid);
		String time = dao.userDate(userid);
		model.addAttribute("time", time);
		model.addAttribute("checkCount", checkCount);
		model.addAttribute("userid",userid);
		return "IfBoard";
		
	}
	@RequestMapping("/Sort_items")
	public String Sort_items(HttpServletRequest req, Model model){
		String sort = req.getParameter("sort");
		System.out.println(sort);
		
		if(sort.equals("viewDsc")) {
			BoardDao dao = sqlSession.getMapper(BoardDao.class);
			ArrayList<BoardDto> ViewDescList = dao.ViewDescList();
			model.addAttribute("ViewDescList", ViewDescList);
			return "viewDsc";
			
		}else if(sort.equals("viewAsc")) {
				BoardDao dao = sqlSession.getMapper(BoardDao.class);
				ArrayList<BoardDto> ViewAsc = dao.ViewAscList();
				model.addAttribute("ViewAsc",ViewAsc );
				return "viewAsc";
			
		}else if(sort.equals("dateDsc")) {
				BoardDao dao = sqlSession.getMapper(BoardDao.class);	
				ArrayList<BoardDto>ViewDateDsc = dao.ViewDateDsc();
				model.addAttribute("ViewDateDsc", ViewDateDsc);
				return "viewDateDsc";
		}else if(sort.equals("dateAsc")) {
				BoardDao dao = sqlSession.getMapper(BoardDao.class);	
				ArrayList<BoardDto>ViewDateAsc = dao.ViewDateAsc();
				model.addAttribute("ViewDateAsc", ViewDateAsc);
				return "viewDateAsc";
		}
		return sort;
		
			
	}
	@RequestMapping("/RankingPage")
	public String RankingPage(HttpServletRequest req, Model model) {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		String Hit = dao.firstRanking();
		String nClick = dao.firstRankingNclick();
		ArrayList<BoardDto> UserRanking = dao.UserRanking();
		model.addAttribute("UserRanking", UserRanking);
		Rankingid = Hit;
		int userCount = dao.UserCount();
		String Rcount = dao.RankingCount(Rankingid);
		model.addAttribute("userCount",userCount);
		model.addAttribute("Rcount", Rcount);
		model.addAttribute("nClick",nClick);
		model.addAttribute("Hit", Hit);
		return "RankingPage";
	}
	
	//���� ������ �˻��ÿ�//
	@RequestMapping("/UserNotFound")
	public String UserNotFound() {
		return "UserNotFound";
	}
	// report//
	@RequestMapping("/report")
	public String report(HttpServletRequest req, Model model) {
		System.out.println("��۾���()");
		int board_num = Integer.parseInt(req.getParameter("board_num"));
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		BoardDto dto = dao.board_view(board_num);
		dao.upHit(board_num);
		System.out.println(board_num);
		model.addAttribute("reply_view", dto);
		model.addAttribute("Userid", Userid);
		dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dtos = dao.list();
		model.addAttribute("dtos", dtos);

		return "report";
	}

	@RequestMapping("/try_report")
	public String Doreport(HttpServletRequest req, Model model) {
		// int board_num = Integer.parseInt(req.getParameter("board_num"));
		String userid = req.getParameter("userid");
		String boardTitle = req.getParameter("boardTitle");
		String board_comment = req.getParameter("board_comment");
		String report_comment = req.getParameter("report_comment");

		System.out.println(userid);
		System.out.println(boardTitle);
		System.out.println(board_comment);
		System.out.println(report_comment);
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		dao.try_report(userid, boardTitle, board_comment, report_comment);
		// int board_num = Integer.parseInt(req.getParameter("board_num"));
		/*
		 * BoardDao dao = sqlSession.getMapper(BoardDao.class); BoardDto dto =
		 * dao.contentView(board_num);
		 * 
		 * model.addAttribute("content_view",dto);
		 */

		return "redirect:board";
	}
	@RequestMapping("/SearchBoard")
	public String SearchBoard(HttpServletRequest req, Model model) {
		String boardtopic = req.getParameter("boardtopic");
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dtos = dao.SearchBoard(boardtopic);
		model.addAttribute("dtos", dtos);
		return "SearchBoard";
	}
	@RequestMapping("/board")
	public String list(Model model) {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dtos = dao.list();
		model.addAttribute("dtos", dtos);
		return "board";

	}

	@RequestMapping("/finduser")
	public String finduser(HttpServletRequest req, Model model) {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dtos = dao.list();
		model.addAttribute("dtos", dtos);
		return "finduser";
	}

	@RequestMapping("/userinfomation")
	public String doFindUser(HttpServletRequest req, Model model) {
		
		//==> �˻� �� ���� �Խñ� �˻��ϱ�//
		String userid = req.getParameter("userid");
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		int checkCount = dao.memberCheck(userid);
		if(checkCount>=1) {
			ArrayList<BoardDto> dtos = dao.userlist(userid);
		    int cnt = dao.userCount(userid);
		    String rdate = dao.userDate(userid);
		    model.addAttribute("rdate",rdate);
		    model.addAttribute("cnt",cnt);
			model.addAttribute("dtos", dtos);
			//==>userid ǥ�� //
			model.addAttribute("userid",userid);
			
			return "userinfomation";
		}else {
			model.addAttribute("userid",userid);
			return "UserNotFound";
		}
		
		
	}

	@RequestMapping("/previous")
	public String previous(Model model) {

		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dtos = dao.previous();
		model.addAttribute("dtos", dtos);
		return "board";

	}

	@RequestMapping("/board_new")
	public String board_new(HttpServletRequest req, Model model) {
		// ==>�α����� �ؾ� �Խñ� �ۼ� �������� redirect
		/*
		 * HttpSession session =req.getSession(); if(session.getAttribute("uid")==null
		 * || session.getAttribute("uid").equals("")) { return"login"; }
		 */

		return "board_new";

	}

	@RequestMapping(value = "/board_write_view", method = RequestMethod.POST)
	public String board_write_view(HttpServletRequest req, Model model) {
		String boardtopic = req.getParameter("boardtopic");
		String userid = req.getParameter("userid");
		String board_comment = req.getParameter("board_comment");
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		dao.write(boardtopic, userid, board_comment);
		return "redirect:board";
	}

	// ==> board_view ���� ��� ���� ,���� , �������
	@RequestMapping(value = "/board_view")
	public String board_view(HttpServletRequest req, Model model) {
		// ��۾��⸦ �����ִ� ������//
		/*
		 * System.out.println("content_view()"); // <a
		 * href="content_view?bId=${dto.bId}">${dto.bTitle}</a> �� �̿��ϸ��// // queryString
		 * ==> String type ==> parseInt( ); int
		 * bId=Integer.parseInt(req.getParameter("bId")); //==>interface Dao <==// IDao
		 * dao = sqlSession.getMapper(IDao.class);
		 * 
		 * BDto dto = dao.contentView(bId); model.addAttribute("reply_view",dto); return
		 * "reply_view";
		 */

		System.out.println("��۾���()");
		int board_num = Integer.parseInt(req.getParameter("board_num"));
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		BoardDto dto = dao.board_view(board_num);
		dao.upHit(board_num);
		System.out.println(board_num);
		model.addAttribute("reply_view", dto);
		model.addAttribute("Userid", Userid);
		dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dtos = dao.list();
		model.addAttribute("dtos", dtos);
		return "board_view";
	}

	// test ��
	@RequestMapping("/reply_view")
	public String reply_view(HttpServletRequest req, Model model) {
		System.out.println("content_view()");
		// <a href="content_view?bId=${dto.bId}">${dto.bTitle}</a> �� �̿��ϸ��//
		// queryString ==> String type ==> parseInt( );
		int board_num = Integer.parseInt(req.getParameter("board_num"));
		// ==>interface Dao <==//
		BoardDao dao = sqlSession.getMapper(BoardDao.class);

		BoardDto dto = dao.contentView(board_num);
		model.addAttribute("reply_view", dto);
		return "reply_view";
	}

	// ����ۼ��ϱ� //
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(HttpServletRequest req, Model model) {
		/*
		 * HttpSession session =req.getSession(); if(session.getAttribute("uid")==null
		 * || session.getAttribute("uid").equals("")) { return"login"; }
		 */

		String board_num = req.getParameter("board_num");
		String bGroup = req.getParameter("bGroup");
		String bStep = req.getParameter("bStep");
		String bIndent = req.getParameter("bIndent");
		String userid = req.getParameter("userid");
		String boardtopic = req.getParameter("boardtopic");
		String board_comment = req.getParameter("board_comment");
		System.out.println(board_num);
		System.out.println(bGroup);
		System.out.println(bStep);
		System.out.println(bIndent);
		System.out.println(userid);
		System.out.println(boardtopic);
		System.out.println(board_comment);

		// ==>interface Dao <==//
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		// dao.reply(bName,bTitle,bContent,Integer.parseInt(bGroup),Integer.parseInt(bStep)+1,Integer.parseInt(bIndent)+1);
		dao.reply(userid, boardtopic, board_comment, Integer.parseInt(bGroup), Integer.parseInt(bStep) + 1,
				Integer.parseInt(bIndent) + 1);
		// dao.replyShape(Integer.parseInt(bGroup),Integer.parseInt(bStep));
		dao.replyShape(Integer.parseInt(bGroup), Integer.parseInt(bStep));

		return "redirect:board";
	}

	@RequestMapping(value = "/board_update")
	public String board_update(HttpServletRequest req, Model model) {
		/*
		 * HttpSession session = req.getSession(); if(session.getAttribute("uid") ==
		 * null ||session.getAttribute("uid").equals("")) { return "redirect:login"; }
		 */
		int board_num = Integer.parseInt(req.getParameter("board_num"));
		// ==>interface Dao <==//
		System.out.println(board_num);
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		System.out.println("debug1");
		BoardDto dto = dao.board_view(board_num);
		System.out.println("debug2");
		model.addAttribute("board_update", dto);
		System.out.println("debug3");
		return "board_update";
	}

	// ==>board_update view==> ���� , ���� ���ÿ� ����<==//
	@RequestMapping(value = "/Doboard_update")
	public String Doboard_update(HttpServletRequest req, Model model) {
		/*
		 * HttpSession session = req.getSession(); if(session.getAttribute("uid") ==
		 * null ||session.getAttribute("uid").equals("")) { return "redirect:login"; }
		 */
		System.out.println("content_view()");
		// ==>���� �������� �����ϰ� �ϱ����ؼ��� �Խù� ��ȣ�� getParameter( ) �� ���ؼ� , where �� �� ���� �ؾ��Ѵ�//

		String hidden = req.getParameter("hidden");
		System.out.println(hidden);
		if (hidden.equals("modify")) {
			String board_num = req.getParameter("board_num");
			String boardtopic = req.getParameter("boardtopic");
			String userid = req.getParameter("userid");
			String board_comment = req.getParameter("board_comment");
			System.out.println(board_num);
			System.out.println(boardtopic);
			System.out.println(userid);
			System.out.println(board_comment);

			// ==>interface Dao <==//
			BoardDao dao = sqlSession.getMapper(BoardDao.class);

			dao.Doboard_update(Integer.parseInt(board_num), boardtopic, userid, board_comment);
			model.addAttribute("board_view", dao);
			return "redirect:board";
		} else {

			String board_num = req.getParameter("board_num");

			// ==>interface Dao <==//
			BoardDao dao = sqlSession.getMapper(BoardDao.class);

			dao.Doboard_delete(Integer.parseInt(board_num));
			model.addAttribute("board_view", dao);
			return "redirect:board";

		}

	}

	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
	public String loginCheck(HttpServletRequest req, Model model) {
		String id = req.getParameter("id");
		String passwd = req.getParameter("passwd");
		// session �̿�
		HttpSession session = req.getSession();
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		int cnt = dao.loginCheck(id, passwd);
		if (cnt == 1) {
			// ==> ȸ������ ���� //
			// ==> uid�����
			session.setAttribute("uid", id);
			Userid = id;
		} else {
			// ==>ȸ������ ����
			return "redirect:login";
		}
		return "redirect:list";
	}

	@RequestMapping("/logout")
	// �α׾ƿ��Ҷ�
	public String doLogout(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		// �α׾ƿ��� �������Ḧ �ϰ� ����Ʈ�� ���ư���
		session.invalidate();
		model.addAttribute("logout", "Y");
		return "redirect:/list";

	}

	@RequestMapping("AboutUs")
	public String AboutUs() {
		return "AboutUs";

	}

	@RequestMapping("email_check")
	public String email_check() {
		return "email_check";

	}

	@RequestMapping("getId")
	public String getId() {
		return "getId";

	}

	@RequestMapping("getPw")
	public String getPw() {
		return "getPw";

	}

	@RequestMapping("home")
	public String home() {
		return "home";

	}

	@RequestMapping("login")
	public String login() {
		return "login";

	}

	@RequestMapping("/myInfo")
	public String myInfo(Model model) {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> dto = dao.list();
		model.addAttribute("dto", dto);
		return "myInfo";
	}

	@RequestMapping("myInfoEdit")
	public String myInfoEdit() {
		return "myInfoEdit";

	}

	@RequestMapping("myItems")
	public String myItems() {
		return "myItems";

	}

	@RequestMapping("paint_new")
	public String paint_new() {
		return "paint_new";

	}

	@RequestMapping("paint")
	public String paint() {
		return "paint";

	}

	@RequestMapping("Signup")
	public String Signup() {
		return "Signup";

	}

	@RequestMapping("welcomeSignup")
	public String welcomeSignup() {
		return "welcomeSignup";

	}

	@RequestMapping("myList")
	public String myList() {
		return "myList";

	}

}
