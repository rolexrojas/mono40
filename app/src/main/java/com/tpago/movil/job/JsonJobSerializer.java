package com.tpago.movil.job;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue;
import com.google.gson.Gson;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hecvasro
 */
final class JsonJobSerializer implements SqliteJobQueue.JobSerializer {

  private static final String CHAR_SET = "UTF-8";

  static Builder builder() {
    return new Builder();
  }

  private final Gson gson;

  private final Map<Class<? extends BaseJob>, String> jobTypes;
  private final Map<String, Class<? extends BaseJob>> jobClasses;

  private JsonJobSerializer(Builder builder) {
    this.gson = builder.gson;

    this.jobTypes = builder.jobTypes;
    this.jobClasses = builder.jobClasses;
  }

  @Override
  public byte[] serialize(Object object) throws IOException {
    ObjectHelper.checkNotNull(object, "object");
    if (!(object instanceof BaseJob)) {
      throw new IllegalArgumentException("!(object instanceof BaseJob)");
    }
    final BaseJob job = (BaseJob) object;
    final Class<? extends BaseJob> jobType = job.getClass();
    if (!this.jobTypes.containsKey(jobType)) {
      throw new IllegalArgumentException(
        String.format("!this.jobTypes.containsType(\"%1$s\")", jobType)
      );
    }
    final BaseJobWrapper jobWrapper = BaseJobWrapper.builder()
      .type(this.jobTypes.get(jobType))
      .data(this.gson.toJson(job, jobType))
      .build();
    return this.gson.toJson(jobWrapper, BaseJobWrapper.class)
      .getBytes(CHAR_SET);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Job> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    ObjectHelper.checkNotNull(bytes, "bytes");
    final BaseJobWrapper jobWrapper = this.gson.fromJson(
      new String(bytes, CHAR_SET),
      BaseJobWrapper.class
    );
    if (!this.jobClasses.containsKey(jobWrapper.type())) {
      throw new IllegalArgumentException(
        String.format("!this.jobClasses.containsType(\"%1$s\")", jobWrapper.type())
      );
    }
    return (T) this.gson.fromJson(jobWrapper.data(), this.jobClasses.get(jobWrapper.type()));
  }

  static final class Builder {

    private Gson gson;

    private final Map<Class<? extends BaseJob>, String> jobTypes;
    private final Map<String, Class<? extends BaseJob>> jobClasses;

    private Builder() {
      this.jobTypes = new HashMap<>();
      this.jobClasses = new HashMap<>();
    }

    final Builder gson(Gson gson) {
      this.gson = ObjectHelper.checkNotNull(gson, "gson");
      return this;
    }

    final Builder addJob(Class<? extends BaseJob> clazz, String type) {
      ObjectHelper.checkNotNull(clazz, "clazz");
      StringHelper.checkIsNotNullNorEmpty(type, "type");
      this.jobTypes.put(clazz, type);
      this.jobClasses.put(type, clazz);
      return this;
    }

    final JsonJobSerializer build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("gson", ObjectHelper.isNull(this.gson))
        .addPropertyNameIfMissing("jobTypes", this.jobTypes.isEmpty())
        .addPropertyNameIfMissing("jobClasses", this.jobClasses.isEmpty())
        .checkNoMissingProperties();
      return new JsonJobSerializer(this);
    }
  }
}
