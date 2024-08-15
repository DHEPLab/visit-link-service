package edu.stanford.fsi.reap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Updates {

  private boolean updated;

  private LocalDateTime lastModifiedAt;

  public static Updates isTheLatest() {
      return new Updates(false, null);
    }

  public static Updates haveUpdate(LocalDateTime lastModifiedAt) {
      return new Updates(true, lastModifiedAt);
    }

}