package com.cq.start.response;

import com.cq.start.domain.enums.MsgCode;

public class Result {

	private Meta meta;
	private Object data;
	private String status;
	private Object msg;
	
	public Result success() {  
        this.meta = new Meta();  
        return this;  
    }  
  
    public Result success(Object data) {  
        this.success();
        this.msg = data;
        return this;  
    } 
    
    public Result setData(Object data) {
        this.data = data;
        return this;  
    }  
  
    public Result failure(Integer code, String msg) {  
        this.meta = new Meta(code, msg);  
        return this;  
    }
    
    public Result failure(Integer code) {
        this.meta = new Meta(code, "","");
        return this;  
    }
    
    public Result failure(MsgCode msgCode) {
    	this.meta = new Meta(msgCode.getCode(), msgCode.getName(),"");
        return this;
    }
  
	public Meta getMeta() {  
        return meta;  
    }  
  
    public Object getData() {  
        return data;  
    }  
    
	public Result setStatus(String status) {
	    this.status = status;
	    return this;
    }
	
    public String getStatus() {
        return status;
    }

	public class Meta {
		private Integer errorNO = 0;
		private String errorInfo = "";
		private String msg ="";

		public Meta() { }  
  
        public Meta(Integer no, String message,String msg) {
            this.errorNO = no;  
            this.errorInfo = message;
            this.msg =msg;
        }
        public Meta(Integer no, String message) {
            this.errorNO = no;
            this.errorInfo = message;
        }
        public Meta( Object message) {
            this.msg =msg;
        }

        public boolean isSuccess() {  
            return errorNO == 0;  
        }  
		
		public Integer getErrorNO() {
			return errorNO;
		}

		public String getErrorInfo() {
			return errorInfo;
		}

	}

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
