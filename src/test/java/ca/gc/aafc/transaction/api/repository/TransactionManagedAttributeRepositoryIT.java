package ca.gc.aafc.transaction.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.TransactionModuleApiLauncher;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionManagedAttributeFixture;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;
import lombok.SneakyThrows;

@SpringBootTest(classes = {TransactionModuleApiLauncher.class, BaseIntegrationTest.TestConfig.class},
  properties = "keycloak.enabled=true")
public class TransactionManagedAttributeRepositoryIT extends TransactionModuleBaseRepositoryIT {

  private static final String BASE_URL = "/api/v1/" + TransactionManagedAttributeDto.TYPENAME;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Inject
  private TransactionManagedAttributeRepository managedResourceRepository;

  private static final String VALID_GROUP = "validGroup";
  private static final String INVALID_GROUP = "invalidGroup";
  private static final String USER_NAME = "testUser";

  @Autowired
  protected TransactionManagedAttributeRepositoryIT(ObjectMapper objMapper) {
    super(BASE_URL, objMapper);
  }

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Override
  protected MockMvc getMockMvc() {
    return mockMvc;
  }

  @Test
  @SneakyThrows
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":SUPER_USER", username = USER_NAME)
  public void findManagedAttribute_whenNoFieldsAreSelected_manageAttributeReturnedWithAllFields() {

    TransactionManagedAttributeDto testManagedAttribute = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
        .group(VALID_GROUP)
        .acceptedValues(new String[] { "dosal" })
        .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.STRING)
        .build();

    JsonApiDocument docToCreate = dtoToJsonApiDocument(testManagedAttribute);
    var postResponse = sendPost(docToCreate);
    JsonApiDocument createdApiDoc = toJsonApiDocument(postResponse);

    TransactionManagedAttributeDto managedAttributeDto = managedResourceRepository.getOne(
      createdApiDoc.getId(), null
    ).getDto();

    assertNotNull(managedAttributeDto);
    assertEquals(createdApiDoc.getId(), managedAttributeDto.getUuid());
    assertArrayEquals(testManagedAttribute.getAcceptedValues(),
        managedAttributeDto.getAcceptedValues());
    assertEquals(testManagedAttribute.getVocabularyElementType(),
        managedAttributeDto.getVocabularyElementType());
    assertEquals(testManagedAttribute.getName(), managedAttributeDto.getName());
    assertEquals(testManagedAttribute.getMultilingualDescription().getDescriptions().get(0),
        managedAttributeDto.getMultilingualDescription().getDescriptions().get(0));
  }

  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":SUPER_USER", username = USER_NAME)
  public void create_WithAuthenticatedUser_SetsCreatedBy() throws Exception {
    TransactionManagedAttributeDto ma = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
      .group(VALID_GROUP)
      .build();

    JsonApiDocument docToCreate = dtoToJsonApiDocument(ma);
    var postResponse = sendPost(docToCreate);
    JsonApiDocument createdApiDoc = toJsonApiDocument(postResponse);

    TransactionManagedAttributeDto managedAttributeDto = managedResourceRepository.getOne(
      createdApiDoc.getId(), null
    ).getDto();

    assertEquals(USER_NAME, managedAttributeDto.getCreatedBy());
  }

  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":SUPER_USER", username = USER_NAME)
  public void update_WithIncorrectGroup_AccessDeniedException() throws Exception {
    TransactionManagedAttributeDto ma = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
      .group(INVALID_GROUP)
      .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.STRING)
      .acceptedValues(new String[] { "dosal" })
      .build();

    JsonApiDocument docToCreate = dtoToJsonApiDocument(ma);
    var exception = assertThrows(org.springframework.web.util.NestedServletException.class, () -> sendPost(docToCreate));
    assertEquals(AccessDeniedException.class, exception.getCause().getClass());
  }
}
