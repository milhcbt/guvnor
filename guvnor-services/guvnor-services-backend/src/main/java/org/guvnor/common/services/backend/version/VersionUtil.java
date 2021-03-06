/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.guvnor.common.services.backend.version;

import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import javax.inject.Named;

import org.uberfire.io.IOService;
import org.uberfire.java.nio.base.AbstractPath;
import org.uberfire.java.nio.file.Path;

class VersionUtil {

    @Inject
    private @Named("ioStrategy") IOService ioService;

    Path getDotFilePath(Path path) {
        return path.resolveSibling("." + path.getFileName());
    }

    String getVersion(Path path) {
        if (path instanceof AbstractPath) {
            String host = ((AbstractPath) path).getHost();
            return host.substring(0, host.indexOf("@"));
        } else {
            return "master";
        }
    }

    Path getPath(Path path, String version) throws URISyntaxException {

        String authority = path.toUri().getAuthority(); // master@uf-playground
        authority = version + authority.substring(authority.indexOf("@"));

        String scheme = path.getFileSystem().provider().getScheme(); // git

        String rawPath = path.toUri().getRawPath(); // projectname/org/something/file.txt

        String uri = scheme +"://"+ authority + rawPath;

        return ioService.get(new URI(uri));
    }

    String getFileName(Path path) {
        return path.getFileName().toString();
    }
}
