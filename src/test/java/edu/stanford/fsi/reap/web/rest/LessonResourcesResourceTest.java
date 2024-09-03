package edu.stanford.fsi.reap.web.rest;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.stanford.fsi.reap.dto.AppOfflineLessonDTO;
import edu.stanford.fsi.reap.dto.Updates;
import edu.stanford.fsi.reap.entity.Lesson;
import edu.stanford.fsi.reap.entity.Module;
import edu.stanford.fsi.reap.entity.enumerations.CurriculumBranch;
import edu.stanford.fsi.reap.repository.LessonRepository;
import edu.stanford.fsi.reap.repository.ModuleRepository;
import edu.stanford.fsi.reap.service.LessonService;
import edu.stanford.fsi.reap.service.ModuleService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
class LessonResourcesResourceTest {

  private static final ModuleRepository moduleRepository = mock(ModuleRepository.class);
  private static final LessonRepository lessonRepository = mock(LessonRepository.class);
  private static final LessonService lessonService = mock(LessonService.class);
  @InjectMocks private static LessonResourcesResource lessonResourcesResource;
  private static MockMvc mockMvc;

  @BeforeAll
  public static void beforeAll() {
    lessonResourcesResource =
        new LessonResourcesResource(
            mock(ModuleService.class), moduleRepository, lessonRepository, lessonService);
    mockMvc = MockMvcBuilders.standaloneSetup(lessonResourcesResource).build();
  }

  @Test
  public void should_is_the_latest() {
    Module module = new Module();
    module.setLastModifiedAt(LocalDateTime.of(2020, 1, 2, 10, 0));
    Lesson lesson = new Lesson();
    lesson.setLastModifiedAt(LocalDateTime.of(2020, 1, 2, 10, 1));

    when(moduleRepository.findFirstByBranchAndPublishedTrueOrderByLastModifiedAtDesc(any()))
        .thenReturn(Optional.of(module));

    when(lessonRepository
            .findFirstByCurriculumBranchAndCurriculumPublishedTrueOrderByLastModifiedAtDesc(any()))
        .thenReturn(Optional.of(lesson));

    Updates updates = lessonResourcesResource.checkForUpdates(null);
    assertFalse(updates.isUpdated());
    updates = lessonResourcesResource.checkForUpdates(LocalDateTime.of(2020, 1, 2, 10, 2));
    assertFalse(updates.isUpdated());
  }

  @Test
  public void should_have_update() {
    Module module = new Module();
    module.setLastModifiedAt(LocalDateTime.of(2020, 1, 2, 10, 0));
    Lesson lesson = new Lesson();
    lesson.setLastModifiedAt(LocalDateTime.of(2020, 1, 2, 10, 0));

    when(moduleRepository.findFirstByBranchAndPublishedTrueOrderByLastModifiedAtDesc(any()))
        .thenReturn(Optional.of(module));

    when(lessonRepository
            .findFirstByCurriculumBranchAndCurriculumPublishedTrueOrderByLastModifiedAtDesc(any()))
        .thenReturn(Optional.of(lesson));
    Updates updates = lessonResourcesResource.checkForUpdates(LocalDateTime.of(2020, 1, 2, 9, 0));
    assertTrue(updates.isUpdated());
  }

  @Test
  @WithMockUser
  public void should_check_forUpdates_isTheLatest() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/resources/check-for-updates")
                .param("lastUpdateAt", "2020-10-10T12:12:12"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.updated").value(false));
  }

  @Test
  @WithMockUser
  public void should_check_forUpdates_lastLesson_haveUpdate() throws Exception {
    Lesson lesson = new Lesson();
    lesson.setLastModifiedAt(LocalDateTime.now());
    when(lessonRepository
            .findFirstByCurriculumBranchAndCurriculumPublishedTrueOrderByLastModifiedAtDesc(
                CurriculumBranch.MASTER))
        .thenReturn(Optional.of(lesson));
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/resources/check-for-updates")
                .param("lastUpdateAt", "2020-10-10T12:12:12"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.updated").value(true));
  }

  @Test
  @WithMockUser
  public void should_check_forUpdates_lastModule_haveUpdate() throws Exception {
    Lesson lesson = new Lesson();
    lesson.setLastModifiedAt(LocalDateTime.now());

    when(lessonRepository
            .findFirstByCurriculumBranchAndCurriculumPublishedTrueOrderByLastModifiedAtDesc(
                CurriculumBranch.MASTER))
        .thenReturn(Optional.of(lesson));

    Module module = new Module();
    module.setLastModifiedAt(LocalDateTime.now());

    when(moduleRepository.findFirstByBranchAndPublishedTrueOrderByLastModifiedAtDesc(
            CurriculumBranch.MASTER))
        .thenReturn(Optional.of(module));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/resources/check-for-updates")
                .param("lastUpdateAt", "2020-10-10T12:12:12"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.updated").value(true));
  }

  @Test
  @WithMockUser
  public void should_downloadModules() throws Exception {
    when(moduleRepository.findByBranchAndPublishedTrue(CurriculumBranch.MASTER))
        .thenReturn(new ArrayList<Module>());

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/resources/modules"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()));
  }

  @Test
  @WithMockUser
  public void should_downloadLessons() throws Exception {
    ArrayList<Lesson> lessons = new ArrayList<>();

    when(lessonRepository.findByCurriculumBranchAndCurriculumPublishedTrue(CurriculumBranch.MASTER))
        .thenReturn(lessons);

    ArrayList<Module> modules = new ArrayList<>();

    when(moduleRepository.findByBranchAndPublishedTrue(CurriculumBranch.MASTER))
        .thenReturn(modules);

    when(lessonService.appOfflineLessons(lessons, modules))
        .thenReturn(new ArrayList<AppOfflineLessonDTO>());

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/resources/lessons"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()));
  }
}
