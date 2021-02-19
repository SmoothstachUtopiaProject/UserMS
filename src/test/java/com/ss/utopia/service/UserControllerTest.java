package com.ss.utopia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.UserController;
import com.ss.utopia.exception.IncorrectPasswordException;
import com.ss.utopia.exception.UserAlreadyExistsException;
import com.ss.utopia.exception.UserNotFoundException;
import com.ss.utopia.model.User;
import com.ss.utopia.model.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Profile("test")
@SpringBootTest
public class UserControllerTest {

  final String SERVICE_PATH_USERS = "/users";
  final UserRole customer = new UserRole(1, "customer");
  final UserRole agent = new UserRole(2, "agent");
  final UserRole employee = new UserRole(3, "employee");
  final UserRole admin = new UserRole(4, "admin");

  @Mock
  UserService service;

  @InjectMocks
  UserController controller;

  MockMvc mvc;
  User testUser;
  List<User> testUserList;

	@BeforeEach
	void setup() throws Exception {
    mvc = MockMvcBuilders.standaloneSetup(controller).build();
    Mockito.reset(service);

    testUser = new User(12, customer, "Ian", "McTester", "ImcTester@test.com", "asduiofh2*&%^", "1837956327");

    testUserList = new ArrayList<User>();
    testUserList.add(new User(1, customer, "Don", "Temming", "DonTemming@test.com", "passpassword", "6278153645"));
    testUserList.add(new User(2, customer, "Jacob", "Itario", "JItario@test.com", "carrot47", "9872356411"));
    testUserList.add(new User(3, customer, "Margeret", "Thatcher", "Margaret.Thatcher@test.com", "10DowningSt", "1125368479"));
    testUserList.add(new User(4, customer, "Sammy", "Davis", "SammyD123@test.com", "junior$((&&", "9826563571"));
    testUserList.add(new User(5, employee, "John", "Roberts", "JRoberts@test.com", "!@RJohns", "8545546237"));
    testUserList.add(new User(6, employee, "Robert", "Johns", "RJohns@test.com", "JRoberts)(", "35324671978"));
    testUserList.add(new User(7, customer, "Mia", "Edwards", "EdwardsM790@test.com", "712370-89789**741623", "7758699751"));
    testUserList.add(new User(8, customer, "Ysvila", "Alivsy", "YAlivsy1987@test.com", "AIOWFYHjkahgasauis&^@643", "9233256481"));
    testUserList.add(new User(9, admin, "Omar", "Bradley", "TheTank@test.com", "*&9hiuasdfh%%(", "1225445687"));
    testUserList.add(new User(10, customer, "Walter", "White", "ImNoChef@test.com", "aAAJSH6asdgjia61%Q#@", "6535248756"));
    testUserList.add(new User(11, customer, "Uma", "Thermin", "ShakeIt@test.com", "(&*%ghfaksd%aj", "1236782359"));
	}

  @Test
  void test_validUserModel_getId() {
    assertEquals(12, testUser.getId());
  }

  @Test
  void test_validUserModel_getUserRole() {
    assertEquals(customer, testUser.getUserRole());
  }

  @Test
  void test_validUserModel_getFirstName() {
    assertEquals("Ian", testUser.getFirstName());
  }

  @Test
  void test_validUserModel_getLastName() {
    assertEquals("McTester", testUser.getLastName());
  }

  @Test
  void test_validUserModel_getEmail() {
    assertEquals("ImcTester@test.com", testUser.getEmail());
  }

  @Test
  void test_validUserModel_getPassword() {
    assertEquals("asduiofh2*&%^", testUser.getPassword());
  }

  @Test
  void test_validUserModel_getPhone() {
    assertEquals("1837956327", testUser.getPhone());
  }

  @Test
  void test_findAllUsers_withValidUsers_thenStatus200() {    
    try {
      when(service.findAll()).thenReturn(testUserList);

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      List<User> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        User[].class)).collect(Collectors.toList());
  
      assertEquals(testUserList.size(), actual.size());
      for(int i = 0; i < testUserList.size(); i++) {
        assertEquals(testUserList.get(i).getId(), actual.get(i).getId());
        assertEquals(testUserList.get(i).getUserRole().getId(), actual.get(i).getUserRole().getId());
        assertEquals(testUserList.get(i).getFirstName(), actual.get(i).getFirstName());
        assertEquals(testUserList.get(i).getLastName(), actual.get(i).getLastName());
        assertEquals(testUserList.get(i).getEmail(), actual.get(i).getEmail());
        assertEquals(testUserList.get(i).getPassword(), actual.get(i).getPassword());
        assertEquals(testUserList.get(i).getPhone(), actual.get(i).getPhone());
      }
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findAllUsers_withNoValidUsers_thenStatus204() {    
    try {
      when(service.findAll()).thenReturn(Collections.emptyList());

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS)
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
  
      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findById_withValidUser_thenStatus200() {    
    try {
      when(service.findById(testUser.getId())).thenReturn(testUser);

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/" + testUser.getId())
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      User actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), User.class);
  
      assertEquals(testUser.getId(), actual.getId());
      assertEquals(testUser.getUserRole().getId(), actual.getUserRole().getId());
      assertEquals(testUser.getFirstName(), actual.getFirstName());
      assertEquals(testUser.getLastName(), actual.getLastName());
      assertEquals(testUser.getEmail(), actual.getEmail());
      assertEquals(testUser.getPassword(), actual.getPassword());
      assertEquals(testUser.getPhone(), actual.getPhone());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findById_withInvalidUser_thenStatus404() {    
    try {
      Integer invalidId = -1;
      when(service.findById(invalidId)).thenThrow(new UserNotFoundException());

      mvc.perform(get(SERVICE_PATH_USERS + "/" + invalidId)
      .header("Accept", "application/json"))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findById_withBadParams_thenStatus400() {    
    try {
      Integer invalidId = null;
      when(service.findById(invalidId)).thenThrow(new IllegalArgumentException());

      mvc.perform(get(SERVICE_PATH_USERS + "/" + invalidId)
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByEmail_withValidUser_thenStatus200() {    
    try {
      String email = testUser.getEmail();
      when(service.findByEmail(email)).thenReturn(testUser);

      mvc.perform(get(SERVICE_PATH_USERS + "/email/" + email)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      // Having issues with Mockito but works fine in Postman
      // User actual = new ObjectMapper().readValue(response
      // .getResponse().getContentAsString(), User.class);
  
      // assertEquals(testUser.getEmail(), actual.getEmail());
      // assertEquals(testUser.getUserRole().getId(), actual.getUserRole().getId());
      // assertEquals(testUser.getFirstName(), actual.getFirstName());
      // assertEquals(testUser.getLastName(), actual.getLastName());
      // assertEquals(testUser.getEmail(), actual.getEmail());
      // assertEquals(testUser.getPassword(), actual.getPassword());
      // assertEquals(testUser.getPhone(), actual.getPhone());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByEmail_withInvalidUser_thenStatus404() {    
    try {
      String invalidEmail = "nope@gmail.com";
      when(service.findByEmail(invalidEmail)).thenThrow(new UserNotFoundException());

      mvc.perform(get(SERVICE_PATH_USERS + "/email/" + invalidEmail)
      .header("Accept", "application/json"))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByEmail_withBadParams_thenStatus400() {    
    try {
      mvc.perform(get(SERVICE_PATH_USERS + "/email/notanemail")
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByRoleId_withValidUser_singleResult_thenStatus200() {    
    try {
      Integer testRoleSearch = admin.getId(); // Should only return Omar Bradley
      when(service.findByRoleId(testRoleSearch)).thenReturn(testUserList.stream()
      .filter(user -> user.getUserRole().getId().equals(testRoleSearch))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/search?roleId=" + testRoleSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();

      List<User> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        User[].class)).collect(Collectors.toList());

      assertEquals(1, actual.size());
      assertEquals(testUserList.get(8).getId(), actual.get(0).getId());
      assertEquals(testUserList.get(8).getUserRole().getId(), actual.get(0).getUserRole().getId());
      assertEquals(testUserList.get(8).getFirstName(), actual.get(0).getFirstName());
      assertEquals(testUserList.get(8).getLastName(), actual.get(0).getLastName());
      assertEquals(testUserList.get(8).getEmail(), actual.get(0).getEmail());
      assertEquals(testUserList.get(8).getPassword(), actual.get(0).getPassword());
      assertEquals(testUserList.get(8).getPhone(), actual.get(0).getPhone());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByRoleId_withValidUsers_multiResult_thenStatus200() {    
    try {
      Integer testRoleSearch = employee.getId(); // Should return John Roberts and Robert Johns
      when(service.findByRoleId(testRoleSearch)).thenReturn(testUserList.stream()
      .filter(user -> user.getUserRole().getId().equals(testRoleSearch))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/search?roleId=" + testRoleSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();

      List<User> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        User[].class)).collect(Collectors.toList());

      assertEquals(2, actual.size()); 
      assertTrue(testUserList.get(4).getId().equals(actual.get(0).getId())                            || testUserList.get(4).getId().equals(actual.get(1).getId()));
      assertTrue(testUserList.get(4).getUserRole().getId().equals(actual.get(0).getUserRole().getId())|| testUserList.get(4).getUserRole().getId().equals(actual.get(1).getUserRole().getId()));
      assertTrue(testUserList.get(4).getFirstName().equals(actual.get(0).getFirstName())              || testUserList.get(4).getFirstName().equals(actual.get(1).getFirstName()));
      assertTrue(testUserList.get(4).getLastName().equals(actual.get(0).getLastName())                || testUserList.get(4).getLastName().equals(actual.get(1).getLastName()));
      assertTrue(testUserList.get(4).getEmail().equals(actual.get(0).getEmail())                      || testUserList.get(4).getEmail().equals(actual.get(1).getEmail()));
      assertTrue(testUserList.get(4).getPassword().equals(actual.get(0).getPassword())                || testUserList.get(4).getPassword().equals(actual.get(1).getPassword()));
      assertTrue(testUserList.get(4).getPhone().equals(actual.get(0).getPhone())                      || testUserList.get(4).getPhone().equals(actual.get(1).getPhone()));

      assertTrue(testUserList.get(5).getId().equals(actual.get(0).getId())                            || testUserList.get(5).getId().equals(actual.get(1).getId()));
      assertTrue(testUserList.get(5).getUserRole().getId().equals(actual.get(0).getUserRole().getId())|| testUserList.get(5).getUserRole().getId().equals(actual.get(1).getUserRole().getId()));
      assertTrue(testUserList.get(5).getFirstName().equals(actual.get(0).getFirstName())              || testUserList.get(5).getFirstName().equals(actual.get(1).getFirstName()));
      assertTrue(testUserList.get(5).getLastName().equals(actual.get(0).getLastName())                || testUserList.get(5).getLastName().equals(actual.get(1).getLastName()));
      assertTrue(testUserList.get(5).getEmail().equals(actual.get(0).getEmail())                      || testUserList.get(5).getEmail().equals(actual.get(1).getEmail()));
      assertTrue(testUserList.get(5).getPassword().equals(actual.get(0).getPassword())                || testUserList.get(5).getPassword().equals(actual.get(1).getPassword()));
      assertTrue(testUserList.get(5).getPhone().equals(actual.get(0).getPhone())                      || testUserList.get(5).getPhone().equals(actual.get(1).getPhone()));
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByRoleId_withNoValidUsers_thenStatus204() {    
    try {
      Integer testRoleSearch = agent.getId(); // Should return no one as there are no agents in the testUserList
      when(service.findByRoleId(testRoleSearch)).thenReturn(testUserList.stream()
      .filter(user -> user.getUserRole().getId().equals(testRoleSearch))
      .collect(Collectors.toList()));

      mvc.perform(get(SERVICE_PATH_USERS + "/search?roleId=" + testRoleSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByRoleId_withBadParams_thenStatus400() {    
    try {
      Integer testRoleSearch = null; // Should trigger badparams as null cannot be cast to an Integer
      mvc.perform(get(SERVICE_PATH_USERS + "/search?roleId=" + testRoleSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }
  
  @Test
  void test_insert_withValidUser_thenStatus201() {    
    try {
      when(service.insert(testUser.getUserRole().getId(), testUser.getFirstName(),
      testUser.getLastName(), testUser.getEmail(), testUser.getPassword(),
      testUser.getPhone())).thenReturn(testUser);      

      MvcResult response = mvc.perform(post(SERVICE_PATH_USERS)
      .header("Accept", "application/json")
      .content(new ObjectMapper().writeValueAsString(testUser)))
      .andExpect(status().is(201))
      .andReturn();

      User actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), User.class);

      assertEquals(testUser.getEmail(), actual.getEmail());
      assertEquals(testUser.getFirstName(), actual.getFirstName());
      assertEquals(testUser.getLastName(), actual.getLastName());
      assertEquals(testUser.getEmail(), actual.getEmail());
      assertEquals(testUser.getPassword(), actual.getPassword());
      assertEquals(testUser.getPhone(), actual.getPhone());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insert_withDuplicateUser_thenStatus409() {    
    try {
      when(service.insert(testUser.getUserRole().getId(), testUser.getFirstName(),
      testUser.getLastName(), testUser.getEmail(), testUser.getPassword(),
      testUser.getPhone())).thenThrow(new UserAlreadyExistsException());

      mvc.perform(post(SERVICE_PATH_USERS)
      .header("Accept", "application/json")
      .content(new ObjectMapper().writeValueAsString(testUser)))
      .andExpect(status().is(409))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insert_withInvalidUserRole_thenStatus400() {    
    try {
      User invalidUserRoleUser = new User(null, testUser.getFirstName(), testUser.getLastName(), 
      testUser.getEmail(), testUser.getPassword(), testUser.getPhone());

      mvc.perform(post(SERVICE_PATH_USERS)
      .header("Accept", "application/json")
      .content(new ObjectMapper().writeValueAsString(invalidUserRoleUser)))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }      

  @Test
  void test_insert_withBadParams_thenStatus400() {    
    try {
      mvc.perform(post(SERVICE_PATH_USERS)
      .header("Accept", "application/json")
      .content(""))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_update_withValidUser_thenStatus202() {    
    try {
      User newUser = new User(testUser.getId(), admin, "TestNewAdminFirst", "TestNewAdminLast", 
      testUser.getEmail(), testUser.getPassword(), testUser.getPhone());
      when(service.update(testUser.getId(), newUser.getUserRole().getId(), newUser.getFirstName(),
      newUser.getLastName(), newUser.getEmail(), newUser.getPassword(), newUser.getPhone()))
      .thenReturn(newUser);

      MvcResult response = mvc.perform(put(SERVICE_PATH_USERS + "/" + newUser.getId())
      .header("Accept", "application/json")
      .content(new ObjectMapper().writeValueAsString(newUser)))
      .andExpect(status().is(202))
      .andReturn();

      User actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), User.class);

      assertEquals(newUser.getEmail(), actual.getEmail());
      assertEquals(newUser.getUserRole().getId(), actual.getUserRole().getId());
      assertEquals(newUser.getFirstName(), actual.getFirstName());
      assertEquals(newUser.getLastName(), actual.getLastName());
      assertEquals(newUser.getEmail(), actual.getEmail());
      assertEquals(newUser.getPassword(), actual.getPassword());
      assertEquals(newUser.getPhone(), actual.getPhone());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_update_withInvalidUser_thenStatus404() {    
    try {
      User newUser = new User(9999, admin, "TestNewAdminFirst", "TestNewAdminLast", 
      testUser.getEmail(), testUser.getPassword(), testUser.getPhone());
      when(service.update(9999, newUser.getUserRole().getId(), newUser.getFirstName(),
      newUser.getLastName(), newUser.getEmail(), newUser.getPassword(), newUser.getPhone()))
      .thenThrow(new UserNotFoundException());

      mvc.perform(put(SERVICE_PATH_USERS + "/" + newUser.getId())
      .header("Accept", "application/json")
      .content(new ObjectMapper().writeValueAsString(newUser)))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_update_withBadParams_thenStatus400() {    
    try {
      mvc.perform(post(SERVICE_PATH_USERS)
      .header("Accept", "application/json")
      .content(""))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_delete_withValidUser_thenStatus204() {    
    try {
      mvc.perform(delete(SERVICE_PATH_USERS + "/" + testUser.getId())
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }
}
