/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2019 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.inject.logger;

import com.google.inject.spi.Dependency;
import com.google.inject.spi.ProvisionListener;
import org.slf4j.Logger;

final class LoggerProvisionListener implements ProvisionListener {

  private boolean isLoggerDependency(Dependency<?> dependency) {
    return dependency != null
        && dependency.getKey().getTypeLiteral().getRawType().isAssignableFrom(Logger.class);
  }

  @Override
  public <T> void onProvision(ProvisionInvocation<T> provision) {
    provision
        .getDependencyChain()
        .stream()
        .map(d -> d.getDependency())
        .filter(this::isLoggerDependency)
        .map(d -> d.getInjectionPoint().getDeclaringType().getRawType())
        .forEach(
            type -> {
              LoggerProvider.NAME.set(type.getName());
              try {
                provision.provision();
              } finally {
                LoggerProvider.NAME.remove();
              }
            });
  }
}
