package it.univaq.disim.sa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ESBResponse {
	@JsonProperty("data")
	private ESBResponseInner[] data;
	
	
	@JsonProperty("data")
    public ESBResponseInner[] getdata() {
        return data;
    }

    @JsonProperty("data")
    public void setdata(ESBResponseInner[] data) {
        this.data = data;
    }
}
