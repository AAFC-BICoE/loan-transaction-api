package ca.gc.aafc.transaction.api.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.validation.ValidationErrorsHelper;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.testsupport.factories.AgentRolesFactory;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionFactory;

public class TransactionValidatorTest extends BaseIntegrationTest {

  @Inject
  private TransactionValidator transactionValidator;

  @Inject
  private MessageSource messageSource;

  @Test
  void validate_NoValidationException_ValidationSuccess() {
    Transaction transaction = TransactionFactory.newTransaction().build();

    Errors errors = ValidationErrorsHelper.newErrorsObject(transaction);
    transactionValidator.validate(transaction, errors);
    Assertions.assertEquals(0, errors.getAllErrors().size());
  }

  @Test
  void validate_ContainsDuplicateRoleUUIDs_HasValidationError() {
    String expectedErrorMessage = getExpectedErrorMessage(TransactionValidator.DUPLICATED_UUID_FOUND_KEY);

    // Create a transaction that contains two of the same UUID values.
    UUID duplicatedUUID = UUID.randomUUID();
    Transaction transaction = TransactionFactory.newTransaction()
        .agentRoles(List.of(
          AgentRolesFactory.newAgentRoles()
              .agent(duplicatedUUID)
              .roles(new ArrayList<String>(List.of("Role1", "Role2"))).build(),
          AgentRolesFactory.newAgentRoles()
              .agent(UUID.randomUUID())
              .roles(new ArrayList<String>(List.of("Role1", "Role2"))).build(),
          AgentRolesFactory.newAgentRoles()
              .agent(duplicatedUUID)
              .roles(new ArrayList<String>(List.of("Role1", "Role2"))).build()
        ))
        .build();

    Errors errors = ValidationErrorsHelper.newErrorsObject(transaction);
    transactionValidator.validate(transaction, errors);
    Assertions.assertEquals(1, errors.getAllErrors().size());
    Assertions.assertEquals(expectedErrorMessage, errors.getAllErrors().get(0).getDefaultMessage());
  }

  private String getExpectedErrorMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }
}
