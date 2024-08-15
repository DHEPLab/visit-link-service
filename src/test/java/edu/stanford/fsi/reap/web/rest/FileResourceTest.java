package edu.stanford.fsi.reap.web.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
class FileResourceTest {

  @InjectMocks private static FileResource resource;
  private static MockMvc mockMvc;

  @BeforeAll
  public static void beforeAll() {
    resource = new FileResource();
    mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
  }

  @Test
  @WithMockUser
  public void should_redirectToOss() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/files/{filename}", "filename"))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }
}
