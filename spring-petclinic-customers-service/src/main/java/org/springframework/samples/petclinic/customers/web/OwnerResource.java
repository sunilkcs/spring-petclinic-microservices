/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.customers.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.monitoring.Monitored;
import org.springframework.web.bind.annotation.*;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RequestMapping("/owners")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
class OwnerResource {

    private final OwnerRepository ownerRepository;

    /**
     * Create Owner
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Monitored
    public void createOwner(@Valid @RequestBody Owner owner) {
        ownerRepository.save(owner);
    }

    /**
     * Read single Owner
     */
    @HystrixCommand(fallbackMethod = "findownerFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
    @GetMapping(value = "/{ownerId}")
    public Owner findOwner(@PathVariable("ownerId") int ownerId) {
    	System.out.println("IN THE CUSTOMER SERVICE FINDOWNER BY ID METHOD....");
        return ownerRepository.findById(ownerId).orElse(null);
    }

    /**
     * Read List of Owners
     */
    @HystrixCommand(fallbackMethod = "findallownersFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
    @GetMapping
    public List<Owner> findAll() {
    	System.out.println("IN THE CUSTOMER SERVICE FIND ALL OWNER METHOD....");
        return ownerRepository.findAll();
    }
    
    public Owner findownerFallback(int ownerId)
    {
    	System.out.println("IN THE CUSTOMER SERVICE FINDOWNER BY ID FALLBACK METHOD....");
    	return null;
    }
    
    public List<Owner> findallownersFallback()
    {
    	System.out.println("IN THE CUSTOMER SERVICE FIND ALL OWNER FALLBACK METHOD....");
    	return null;
    }

    /**
     * Update Owner
     */
    @PutMapping(value = "/{ownerId}")
    @Monitored
    public Owner updateOwner(@PathVariable("ownerId") int ownerId, @Valid @RequestBody Owner ownerRequest) {
        final Owner ownerModel = ownerRepository.findById(ownerId).orElse(null);
        // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
        ownerModel.setFirstName(ownerRequest.getFirstName());
        ownerModel.setLastName(ownerRequest.getLastName());
        ownerModel.setCity(ownerRequest.getCity());
        ownerModel.setAddress(ownerRequest.getAddress());
        ownerModel.setTelephone(ownerRequest.getTelephone());
        log.info("Saving owner {}", ownerModel);
        return ownerRepository.save(ownerModel);
    }
}
