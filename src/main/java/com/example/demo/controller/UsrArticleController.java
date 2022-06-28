package com.example.demo.controller;

import java.util.List;

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
	public ResultData doAdd(String title, String body) {
		if(Ut.empty(title)) {
			return ResultData.from("F-1", "title(을)를 입력하세요.");
		}
		
		if(Ut.empty(body)) {
			return ResultData.from("F-1", "body(을)를 입력하세요.");
		}
		
		
		ResultData writeArticleRd = articleService.writeArticle(title, body);
		int id = (int)writeArticleRd.getData1();
		Article article = articleService.getArticle(id);
				
		return ResultData.from(writeArticleRd.getResultCode(), writeArticleRd.getMsg(),article);
	}

	
	
	@RequestMapping("/usr/article/getarticles")
	@ResponseBody
	public ResultData getArticles() {
		List<Article> article = articleService.getArticles();
		return ResultData.from("S-1", "게시물 리스트 입니다.",article);
	}

	@RequestMapping("/usr/article/getarticle")
	@ResponseBody
	public ResultData getArticle(int id) {
		Article article = articleService.getArticle(id);
		
		if(article == null) {	
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다.", id));
		}
		return ResultData.from("S-1", Ut.f("%d번 게시물입니다.", id), article);
	}

	@RequestMapping("/usr/article/doremove")
	@ResponseBody
	public String removeArticle(int id) {
		Article article = articleService.getArticle(id);
		if (article == null) {
			return id + "번 게시물이 없습니다.";
		}
		articleService.deleteArticle(id);
		return id + "번 게시물을 삭제했습니다.";
	}

	@RequestMapping("/usr/article/domodify")
	@ResponseBody
	public String articleModify(int id, String title, String body) {
		Article article = articleService.getArticle(id);
		if (article == null) {
			return id + "번 게시물이 없습니다.";
		}
		articleService.modifyArticle(id, title, body);
		return id + "번 게시물을 수정했습니다.";
	}
}