package edu.stanford.fsi.reap.dto;

import edu.stanford.fsi.reap.entity.Module;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ModulePackage {

  private List<Module> modules;

  private List<String> media;

}