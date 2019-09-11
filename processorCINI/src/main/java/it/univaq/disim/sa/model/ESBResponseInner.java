package it.univaq.disim.sa.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topic",
    "id",
    "user",
    "data",
    "timestamp"
})
public class ESBResponseInner {

    @JsonProperty("topic")
    private String topic;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user")
    private String user;
    @JsonProperty("data")
    private String data;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
