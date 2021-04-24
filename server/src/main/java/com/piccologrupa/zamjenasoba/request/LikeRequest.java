package com.piccologrupa.zamjenasoba.request;

public class LikeRequest {
	
	private Long oglasId;
	private int stupanjLajkanja;

	public LikeRequest(Long oglasId, int stupanjLajkanja) {
		super();
		this.oglasId = oglasId;
		this.stupanjLajkanja = stupanjLajkanja;
	}
	
	public Long getOglasId() {
		return oglasId;
	}
	
	public void setOglasId(Long oglasId) {
		this.oglasId = oglasId;
	}
	
	public int getStupanjLajkanja() {
		return stupanjLajkanja;
	}
	
	public void setStupanjLajkanja(int stupanjLajkanja) {
		this.stupanjLajkanja = stupanjLajkanja;
	}
	
}
