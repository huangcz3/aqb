package com.aqb.cn.action;

import javax.servlet.http.HttpServletRequest;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.utils.UUIDCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aqb.cn.bean.Price;
import com.aqb.cn.bean.Shield;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.ShieldService;

import java.util.Date;
import java.util.Map;

@Controller
public class ShieldAction {
	@Autowired
	ShieldService shieldService;
	
	/*
	 * 新增屏蔽关键词
	 * 后台
	 */
	@AdminLogin
	@ResponseBody
    @RequestMapping(value = "/api/shield/addShield", method = RequestMethod.POST)
	public Object addShield(HttpServletRequest request, @RequestBody Shield shield){
		if(shield.getShieldGuanjianci() == null || shield.getShieldGuanjianci().equals("")){
			return  new Response(Status.ERROR, "屏蔽词不能为空");
		}
		shield.setId(UUIDCreator.getUUID());
		shield.setStatus(1);//状态（0--关闭，1--开启）
		shield.setFoundDate(new Date());

		int status=shieldService.add(shield);
		String message;
		if(status==Status.SUCCESS){
			message="添加成功";
		}else if(status==Status.ERROR){
			message="添加失败";
		}else if(status==Status.EXISTS){
			message="关键词已存在";
		}else{
			message="未知错误";
		}
		
		return  new Response(status, message);
	}
	
	/*
	 * 删除屏蔽关键词
	 * 后台
	 */
	@AdminLogin
	@ResponseBody
    @RequestMapping(value = "/api/shield/deleteShield", method = RequestMethod.POST)
	public Object deleteShield(HttpServletRequest request, @RequestBody Shield shield){
		int status=shieldService.delete(shield);
		String message;
		if(status==Status.SUCCESS){
			message="删除成功";
		}else if(status==Status.ERROR){
			message="删除失败";
		}else{
			message="未知错误";
		} 
		return  new Response(status, message);
	}
	
	/*
	 * 开启/关闭 屏蔽词
	 * 后台
	 */
	@AdminLogin
	@ResponseBody
    @RequestMapping(value = "/api/shield/updateShield", method = RequestMethod.POST)
	public Object updateShield(HttpServletRequest request, @RequestBody Shield shield){
		if(shield.getId() == null || shield.getId().equals("")){
			return  new Response(Status.ERROR,"id不能为空");
		}
		Shield shield_db = shieldService.get(shield.getId());
		if (shield_db.getStatus() == 0) {
			shield_db.setStatus(1);
		}else {
			shield_db.setStatus(0);
		}
		int status=shieldService.update(shield_db);
		String message;
		if(status==Status.SUCCESS){
			message="更新成功";
		}else{
			message="更新失败";
		} 
		return  new Response(status,message);
	}

	/**
	 * 屏蔽关键词列表查询
	 * 后台
	 * @param request
	 * @return
	 */
	@AdminLogin
	@ResponseBody
	@RequestMapping(value = "/api/shield/queryShield", method = RequestMethod.GET)
	public Object queryShield(HttpServletRequest request,
											 @RequestParam(value = "currentPage", required = false) Long currentPage,
											 @RequestParam(value = "maxRow", required = false) Long maxRow) {
		QueryBase query = new QueryBase();
		if (currentPage != null) {
			query.setCurrentPage(currentPage);
		}
		if(maxRow != null){
			query.setMaxRow(maxRow);
		}
		shieldService.query(query);
		return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
	}


}
