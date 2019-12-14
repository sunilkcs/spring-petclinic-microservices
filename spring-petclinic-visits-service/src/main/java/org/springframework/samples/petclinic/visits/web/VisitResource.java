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
package org.springframework.samples.petclinic.visits.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.web.bind.annotation.*;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class VisitResource {

	private final VisitRepository visitRepository;

	@PostMapping("owners/*/pets/{petId}/visits")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void create(@Valid @RequestBody Visit visit, @PathVariable("petId") int petId) {

		visit.setPetId(petId);
		log.info("Saving visit {}", visit);
		visitRepository.save(visit);
	}

	@HystrixCommand(fallbackMethod = "visitsFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	@GetMapping("owners/*/pets/{petId}/visits")
	public List<Visit> visits(@PathVariable("petId") int petId) {
		System.out.println("IN THE VISITS METHOD....");
		return visitRepository.findByPetId(petId);
	}

	public List<Visit> visitsFallback(int petId) {
		System.out.println("IN THE VISITS FALLBACK METHOD....");
		List<Visit> visits = Arrays.asList();
		return visits;
	}
}
