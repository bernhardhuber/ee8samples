/*
 * Copyright 2023 berni3.
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
package org.huberb.ee8sample.fs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author berni3
 */
public class Filesystem {

    public static class Access {

        public static class AccessControl {

            Owner owner = new Owner(Owner.DEFAULT_USER_NAME);
            Group group = new Group(Group.DEFAULT_GROUP_NAME);
            Other other = Other.INSTANCE();

            public boolean isAllowed(String user) {
                boolean result = false;

                result = result || (owner != null && owner.isOwner(user));
                result = result || (group != null && group.isMemberOfGroup(user));
                result = result || (other != null && other.isOther());

                if (result) {
                    return true;
                }

                return false;
            }
        }

        public static class Owner {

            private static final String ROOT_USER = "root";
            private static final String DEFAULT_USER_NAME = "default-user-name";

            private String user;

            public Owner(String user) {
                this.user = user;
            }

            public boolean isOwner(String user) {
                boolean result = false;
                result = result || ROOT_USER.equals(user);
                result = result || DEFAULT_USER_NAME.equals(this.user);
                result = result || (this.user != null && this.user.equals(user));
                return result;
            }
        }

        public static class Group {

            private static final String DEFAULT_GROUP_NAME = "default-name-group";
            private static final Group DEFAULT_GROUP = new Group(DEFAULT_GROUP_NAME);
            private String name;
            private Set<String> members;

            public Group(String name) {
                this.name = name;
                this.members = new HashSet<>(5);
            }

            public boolean isMemberOfGroup(String user) {
                return members.contains(user);
            }

        }

        public static class Other {

            private static final Other INSTANCE = new Other();

            public static final Other INSTANCE() {
                return INSTANCE;
            }

            private Other() {
            }

            public boolean isOther() {
                return true;
            }
        }
    }

    public static class Files {

        @Data
        public static abstract class AbstractFile {

            public abstract String getName();

            public abstract Directory getParent();

            public enum FileType {
                directory, regular
            }

            public abstract FileType getFileType();

        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class Directory extends AbstractFile {

            public static Directory ROOT_DIRECTORY = new Directory("/", null);

            private String name;
            private Directory parent;
            private List<AbstractFile> files;

            public Directory(String name) {
                this(name, ROOT_DIRECTORY);
            }

            public Directory(String name, Directory parent) {
                this.name = name;
                this.parent = parent;
                this.files = new ArrayList<>();
                if (parent != null) {
                    parent.add(this);
                }
            }

            @Override
            public String getName() {
                return this.name;
            }

            @Override
            public Directory getParent() {
                return this.parent;
            }

            @Override
            public FileType getFileType() {
                return FileType.directory;
            }

            public void add(AbstractFile f) {
                if (f != this) {
                    this.files.add(f);
                }
            }
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @AllArgsConstructor
        public static class Regular extends AbstractFile {

            private String name;
            private Directory parent;

            @Override
            public String getName() {
                return this.name;
            }

            @Override
            public Directory getParent() {
                return this.parent;
            }

            @Override
            public FileType getFileType() {
                return FileType.regular;
            }
        }

        @Data
        @Builder
        @AllArgsConstructor
        public static class PayloadMetaInfo {

            private String charset;
            private String encoding;
            private String contentType;
            private Long contentLength;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class RegularPayload<T> extends Regular {

            private T payload;

            public RegularPayload(String name, Directory parent, T payload) {
                super(name, parent);
                this.payload = payload;
            }

            public T getPayload() {
                return payload;
            }
        }

    }
}
