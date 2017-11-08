package com.tpago.movil.partner;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.company.LogoCatalog;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Carrier extends Partner {

  public static PartnerBuilder builder() {
    return new BuilderImpl();
  }

  Carrier() {
  }

  @Override
  public Type type() {
    return Type.CARRIER;
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  static abstract class Builder {

    Builder() {
    }

    abstract Builder id(String id);

    abstract Builder code(int code);

    abstract Builder name(String name);

    abstract Builder logoTemplate(String logoTemplate);

    abstract Builder logoCatalog(LogoCatalog logoHolder);

    abstract Carrier build();
  }

  private static final class BuilderImpl implements PartnerBuilder {

    private final Carrier.Builder builder;

    private BuilderImpl() {
      this.builder = new AutoValue_Carrier.Builder();
    }

    @Override
    public PartnerBuilder id(String id) {
      this.builder.id(id);
      return this;
    }

    @Override
    public PartnerBuilder code(int code) {
      this.builder.code(code);
      return this;
    }

    @Override
    public PartnerBuilder name(String name) {
      this.builder.name(name);
      return this;
    }

    @Override
    public PartnerBuilder logoTemplate(String logoTemplate) {
      this.builder.logoTemplate(logoTemplate);
      return this;
    }

    @Override
    public PartnerBuilder logoCatalog(LogoCatalog logoCatalog) {
      this.builder.logoCatalog(logoCatalog);
      return this;
    }

    @Override
    public Carrier build() {
      return this.builder.build();
    }
  }
}
