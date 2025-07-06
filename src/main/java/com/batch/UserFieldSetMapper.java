package com.batch;

import com.model.User;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class UserFieldSetMapper implements FieldSetMapper<User> {

  @Override
  public User mapFieldSet(FieldSet fieldSet) {
    User user = new User();
    user.setUserId(fieldSet.readInt(0));
    user.setGender(fieldSet.readString(1));
    user.setAge(fieldSet.readInt(2));
    user.setOccupation(fieldSet.readInt(3));
    user.setZipCode(fieldSet.readString(4));
    return user;
  }
}
