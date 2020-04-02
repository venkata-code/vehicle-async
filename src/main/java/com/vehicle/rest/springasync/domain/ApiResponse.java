package com.vehicle.rest.springasync.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ApiResponse {
    private String body;
    private UUID id;
   
    private String status;

    @JsonRawValue
    public String getBody() {
        if(body != null && body.trim().isEmpty()) {
            return null;
        }
        return body;
    }

    
    
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param httpResponse the status to set
	 */
	public void setStatus(String httpResponse) {
		this.status = httpResponse;
	}



	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param uuid the id to set
	 */
	public void setId(UUID uuid) {
		this.id = uuid;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
    
    
    
}
