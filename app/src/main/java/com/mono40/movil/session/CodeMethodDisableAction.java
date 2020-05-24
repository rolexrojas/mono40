package com.mono40.movil.session;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class CodeMethodDisableAction implements UnlockMethodDisableAction {

  static CodeMethodDisableAction create(CodeStore codeStore) {
    return new CodeMethodDisableAction(codeStore);
  }

  private final CodeStore codeStore;

  private CodeMethodDisableAction(CodeStore codeStore) {
    this.codeStore = ObjectHelper.checkNotNull(codeStore, "codeStore");
  }

  @Override
  public void run() throws Exception {
    this.codeStore.clear();
  }
}
