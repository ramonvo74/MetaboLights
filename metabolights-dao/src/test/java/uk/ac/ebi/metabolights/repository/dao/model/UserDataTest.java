/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Jan-19
 * Modified by:   conesa
 *
 *
 * Copyright 2015 EMBL-European Bioinformatics Institute.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package uk.ac.ebi.metabolights.repository.dao.model;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.metabolights.repository.dao.hibernate.HibernateTest;
import uk.ac.ebi.metabolights.repository.dao.hibernate.HibernateUtil;

import java.util.Date;

public class UserDataTest extends HibernateTest{


	@Test
	public void testUserDataCRUD(){

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// Get a new user
		UserData userData = getNewUserData();

		// Test persistence
		session.save(userData);

		Assert.assertNotNull("Id it's been populated", userData.id);
		logger.info("New userData id populated: " + userData.id);

		session.delete(userData);

		session.getTransaction().commit();
	}

	public static UserData getNewUserData() {
		UserData userData = new UserData();

		userData.address = "address";
		userData.affiliation = "affiliation";
		userData.affiliationUrl = "wwwaffiliation";
		userData.apiToken = "token";
		userData.email = "email";
		userData.firstName = "firstName";
		userData.joinDate = new Date();
		userData.lastName = "lastName";
		userData.password = "password";
		userData.role = 1;
		userData.status = 0;
		userData.userName = "username";

		return userData;
	}
}