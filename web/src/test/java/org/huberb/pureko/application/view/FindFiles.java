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

    final Path start;
    final int maxDepth;
    final FileVisitOption[] options;

    public FindFiles(Path start, int maxDepth) {
        this(start, maxDepth, new FileVisitOption[0]);
    }

    public FindFiles(Path start, int maxDepth, FileVisitOption[] options) {
        this.start = start;
        this.maxDepth = maxDepth;
        this.options = options;
    }

    /**
     * Find regular files matching a given extension.
     *
     * @param start base directory
     * @param matchingExt required matched extension
     * @return
     * @throws IOException
     *
     * @see Files#find(java.nio.file.Path, int, java.util.function.BiPredicate,
     * java.nio.file.FileVisitOption...)
     */
    List<Path> findTheFilesReturningList(String matchingExt) throws IOException {
        final Stream<Path> result = findTheFilesReturningStream(matchingExt);
        final List<Path> l = result.collect(Collectors.toList());
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
     * @see Files#find(java.nio.file.Path, int, java.util.function.BiPredicate,
     * java.nio.file.FileVisitOption...)
     */
    Stream<Path> findTheFilesReturningStream(String matchingExt) throws IOException {
        final BiPredicate<Path, BasicFileAttributes> matcher = matchingFilenameEndingWithExtension(matchingExt);
        final Stream<Path> result = findTheFilesMatching(matcher);
        return result;
    }

    /**
     * General find files matching a given {@link Predicate}.
     *
     * @param matchingPred
     * @return
     * @throws IOException
     */
    Stream<Path> findTheFilesMatching(BiPredicate<Path, BasicFileAttributes> matchingPred) throws IOException {
        final BiPredicate<Path, BasicFileAttributes> matcher = matchingPred;
        final Stream<Path> result = Files.find(start, maxDepth, matcher, options);
        return result;
    }

    //---
    static BiPredicate<Path, BasicFileAttributes> matchingFilenameEndingWithExtension(String matchingExt) {
        final BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) -> {
            return bfa.isRegularFile() && p.toFile().getName().endsWith(matchingExt);
        };
        return matcher;
    }
}
