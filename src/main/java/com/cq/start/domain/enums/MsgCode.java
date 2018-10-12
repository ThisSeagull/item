package com.cq.start.domain.enums;

public enum MsgCode {
	
	UN_PERMISSION(-1, "权限不足"),
	PUSER_UN_LOGINED(-2, "个人用户未登录"),
	CUSER_UN_LOGINED(-3, "企业用户未登录"),
	READ_JSON_ERROR(-91, "参数解析失败"),
	MEDIA_TYPE_UNSUPPORT(-92, "不支持当前媒体类型"),
	SERVER_ERROR(-93, "服务运行异常"),
	NOTNULL(-94, "必填字段不能为空"),
	TOKEN_UNVALID(-95, "Token信息不正确"),
	PARAMS_UNCOMPLETE(-96, "参数不全，请检查"),
	PARAMS_WRONG_FORMAT(-97, "参数格式有误"),
	SERVICE_NOTFOUND(-98, "找不到相应的服务"),
	METHOD_EXC_FAILURE(-99, "操作失败");
	
	private int code;
	private String name;
	
	private MsgCode(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public boolean equals(int code) {
		return this.code== code;
	}
}
