package edu.stanford.fsi.reap.web.rest.admin;

import edu.stanford.fsi.reap.service.FileService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class OSSResourceTest {

  @InjectMocks
  private static MockMvc mockMvc;
  private static FileService fileService;
  private static final String url = "/admin/oss";

  @BeforeAll
  public static void beforeAll() {
    fileService = mock(FileService.class);
    OSSResource fileResource = new OSSResource(fileService);
    mockMvc = MockMvcBuilders.standaloneSetup(fileResource).build();
  }

  @Test
  @WithMockUser
  void sign() throws Exception {
    when(fileService.generatePresignedUrlForUpload(any())).thenReturn(new URL("http://test"));

    mockMvc
            .perform(MockMvcRequestBuilders.get(url + "/upload-pre-signed-url").param("format", "Test"))
            .andDo(print())
            .andExpect(status().isOk());
  }
}
