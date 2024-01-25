package ca.gc.aafc.transaction.api.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.security.auth.PermissionAuthorizationService;

@Service
public class TransactionManagedAttributeAuthorizationService extends PermissionAuthorizationService {


  @Override
  @PreAuthorize("hasMinimumGroupAndRolePermissions(@currentUser, 'SUPER_USER', #entity)")
  public void authorizeCreate(Object entity) {
  }

  @Override
  @PreAuthorize("hasMinimumGroupAndRolePermissions(@currentUser, 'SUPER_USER', #entity)")
  public void authorizeUpdate(Object entity) {
  }

  @Override
  @PreAuthorize("hasMinimumGroupAndRolePermissions(@currentUser, 'SUPER_USER', #entity)")
  public void authorizeDelete(Object entity) {
  }

  // Do nothing for now
  @Override
  public void authorizeRead(Object entity) {
  }

  @Override
  public String getName() {
    return "TransactionManagedAttributeAuthorizationService";
  }
}
