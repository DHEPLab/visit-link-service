package edu.stanford.fsi.reap.web.rest.admin;

import static com.aliyun.oss.internal.OSSConstants.DEFAULT_OBJECT_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@AutoConfigureMockMvc
class OSSResourceTest {

  @InjectMocks
  private static MockMvc mockMvc;
  private static OSS oss;
  private static final String url = "/admin/oss";

  @BeforeAll
  public static void berforeAll() {
    oss = mock(OSS.class);
    OSSResource ossResource = new OSSResource(oss);
    mockMvc = MockMvcBuilders.standaloneSetup(ossResource).build();
  }

  @Test
  @WithMockUser
  void sign() throws Exception {
    when(oss.generatePresignedUrl(any())).thenReturn(new URL("http://test"));

    mockMvc
            .perform(MockMvcRequestBuilders.get(url + "/pre-signed-url").param("format", "Test"))
            .andDo(print())
            .andExpect(status().isOk());
  }
}
