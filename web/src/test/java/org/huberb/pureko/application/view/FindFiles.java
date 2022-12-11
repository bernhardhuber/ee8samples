/*
 * Copyright 2022 berni3.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.huberb.pureko.application.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Find files.
 */
class FindFiles {

    /**
     * Find regular files matching a given extension.
     *
     * @param start base directory
     * @param matchingExt required matched extension
     * @return
     * @throws IOException
     *
     * @see Files#find(java.nio.file.Path, int,
     * java.util.function.BiPredicate, java.nio.file.FileVisitOption...)
     */
    static List<Path> findTheFilesReturningList(File start, String matchingExt) throws IOException {
        Stream<Path> result = findTheFilesReturningStream(start, matchingExt);
        List<Path> l = result.collect(Collectors.toList());
        return l;
    }

    /**
     * Find regular files matching a given extension.
     *
     * @param start base directory
     * @param matchingExt required matched extension
     * @return lazy stream of {@link Path}
     * @throws IOException
     *
     * @see Files#find(java.nio.file.Path, int,
     * java.util.function.BiPredicate, java.nio.file.FileVisitOption...)
     */
    static Stream<Path> findTheFilesReturningStream(File start, String matchingExt) throws IOException {
        final BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) -> {
            return bfa.isRegularFile() && p.toFile().getName().endsWith(matchingExt);
        };
        final FileVisitOption[] options = new FileVisitOption[0];
        final int maxDepth = 10;
        final Stream<Path> result = Files.find(start.toPath(), maxDepth, matcher, options);
        return result;
    }
    
}
