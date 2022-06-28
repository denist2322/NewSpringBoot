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

	@RequestMapping("/usr/article/getarticles")
	@ResponseBody
	public List<Article> getArticles() {
		return articleService.getArticles();
	}

	@RequestMapping("/usr/article/getarticle")
	@ResponseBody
	public ResultData getArticle(int id) {
		Article article = articleService.getArticle(id);
		
		if(article == null) {	
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다.", id));
		}
		return article;
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