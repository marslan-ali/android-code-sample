package co.appdev.invited.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import co.appdev.invited.injection.component.ConfigPersistentComponent;


/**
 * A scoping annotation to permit dependencies conform to the life of the
 * {@link ConfigPersistentComponent}
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPersistent {
}
