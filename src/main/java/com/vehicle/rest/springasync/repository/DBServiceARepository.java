/**
* @author Venkata
* @date 03-Apr-2020
*/
/**
 * 
 */
package com.vehicle.rest.springasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vehicle.rest.springasync.domain.VehicleData;

/**
 * @author Venkata
 *
 */
@Repository
public interface DBServiceARepository 
        extends JpaRepository<VehicleData, Long> {
 
}