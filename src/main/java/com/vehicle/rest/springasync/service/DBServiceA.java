/**
* @author Venkata
* @date 03-Apr-2020
*/
/**
 * 
 */
package com.vehicle.rest.springasync.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.vehicle.rest.springasync.domain.VehicleData;
import com.vehicle.rest.springasync.repository.DBServiceARepository;

/**
 * @author Venkata
 *
 */
@Async
public class DBServiceA {
	private static final Logger log = LoggerFactory.getLogger(DBServiceA.class);
	@Autowired
	DBServiceARepository repository;
     
	 public String getData(long id) {
		 String resultData = "";
	        log.debug("retriving Db data");
	        List<VehicleData> vechileData = repository.findAll();
	         
	        if(vechileData.size() > 0) {
	        	resultData = vechileData.get(0).toString();
	        } 
	       
	        
	        log.debug(resultData);
	        return resultData;
	    }
	 
	public VehicleData SaveData(VehicleData vechileData) {

		return repository.save(vechileData);

	}
	}



