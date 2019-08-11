package de.plushnikov.intellij.plugin.processor.field;

import com.passiontec.annotation.BSetter;
import de.plushnikov.intellij.plugin.lombokconfig.ConfigDiscovery;
import org.jetbrains.annotations.NotNull;

/**
 * Inspect and validate @Setter lombok annotation on a field
 * Creates setter method for this field
 *
 * @author Plushnikov Michail
 */
public class BuilderSetterFieldProcessor extends SetterFieldProcessor {

  public BuilderSetterFieldProcessor(@NotNull ConfigDiscovery configDiscovery) {
    super(configDiscovery, BSetter.class);
  }

}
