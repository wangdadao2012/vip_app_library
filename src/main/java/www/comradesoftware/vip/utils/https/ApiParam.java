package www.comradesoftware.vip.utils.https;

public class ApiParam {
	private String Data ;
	private String NonceStr;
	private String SignType;
	private String Sign;
	
	public ApiParam(){
		NonceStr=Helper.getGUID();
	}
 
    public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}

	public String getNonceStr() {
		return NonceStr;
	}

	public void setNonceStr(String nonceStr) {
		NonceStr = nonceStr;
	}

	public String getSignType() {
		return SignType;
	}

	public void setSignType(String signType) {
		SignType = signType;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public void setDataFromObj(Object obj){
    	Data = JsonUtil.toJson(obj);
    }
}
