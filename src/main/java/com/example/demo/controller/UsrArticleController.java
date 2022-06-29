package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.ArticleService;
import com.example.demo.util.Ut;
import com.example.demo.vo.Article;
import com.example.demo.vo.ResultData;

@Controller
public class UsrArticleController {
	@Autowired
	private ArticleService articleService;

	@RequestMapping("/usr/article/doadd")
	@ResponseBody
	public ResultData<Article> doAdd(HttpSession httpSession, String title, String body) {
		boolean isLogined = false;
		int loginedMemberId = 0;

		if (httpSession.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) httpSession.getAttribute("loginedMemberId");
		}

		if (isLogined == false) {
			return ResultData.from("F-A", "로그인 후 이용해주세요.");
		}
		

		if (Ut.empty(title)) {
			return ResultData.from("F-1", "title(을)를 입력하세요.");
		}

		if (Ut.empty(body)) {
			return ResultData.from("F-1", "body(을)를 입력하세요.");
		}

		ResultData<Integer> writeArticleRd = articleService.writeArticle(loginedMemberId, title, body);
		int id = writeArticleRd.getData1();

		Article article = articleService.getArticle(id);

		return ResultData.newData(writeArticleRd, article);
	}

	@RequestMapping("/usr/article/getarticles")
	@ResponseBody
	public ResultData<List<Article>> getArticles() {
		List<Article> article = articleService.getArticles();
		return ResultData.from("S-1", "게시물 리스트 입니다.", article);
	}

	@RequestMapping("/usr/article/getarticle")
	@ResponseBody
	public ResultData getArticle(int id) {
		Article article = articleService.getArticle(id);

		if (article == null) {
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다.", id));
		}
		return ResultData.from("S-1", Ut.f("%d번 게시물입니다.", id), article);
	}

	@RequestMapping("/usr/article/doremove")
	@ResponseBody
	public ResultData<Integer> removeArticle(HttpSession httpSession, int id) {
		boolean isLogined = false;
		int loginedMemberId = 0;

		if (httpSession.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) httpSession.getAttribute("loginedMemberId");
		}

		if (isLogined == false) {
			return ResultData.from("F-A", "로그인 후 이용해주세요.");
		}
		
		
		Article article  = articleService.getArticle(id);
		
		if (article.getMemberId() != loginedMemberId) {
			return ResultData.from("F-2", "권한이 없습니다.");
		}
		
		
		
		if (article == null) {
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다.", id), id);
		}
		articleService.deleteArticle(id);
		return ResultData.from("S-1", Ut.f("%d번 게시물을 삭제했습니다.", id), id);
	}

	@RequestMapping("/usr/article/domodify")
	@ResponseBody
	public ResultData<Integer> articleModify(int id, String title, String body) {
		Article article = articleService.getArticle(id);
		if (article == null) {
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다.", id), id);
		}
		articleService.modifyArticle(id, title, body);
		return ResultData.from("S-1", Ut.f("%d번 게시물을 삭제했습니다.", id), id);
	}
}