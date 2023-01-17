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
package org.huberb.ee8sample.genericdata;

/**
 *
 * @author berni3
 */
public class Files {
    
    public static abstract class AbstractFile {

        abstract String getName();

        enum FileType {
            directory, regular
        }

        abstract FileType getFileType();

        abstract AbstractFile getParent();
    }

    public static class Directory extends AbstractFile {

        String name;

        String getName() {
            return this.name;
        }
        AbstractFile parent;

        AbstractFile getParent() {
            return this.parent;
        }

        FileType getFileType() {
            return FileType.directory;
        }
    }

    public static class Regular extends AbstractFile {

        String name;

        String getName() {
            return this.name;
        }
        AbstractFile parent;

        AbstractFile getParent() {
            return this.parent;
        }

        FileType getFileType() {
            return FileType.regular;
        }
    }

    public static class PayloadMetaInfo {

        String charset;
        String encoding;
        String contentType;
        Long contentLength;
    }

    public static class RegularPayload<T> extends Regular {

        T payload;
        PayloadMetaInfo payloadMetaInfo;

        T getPayload() {
            return payload;
        }
    }
    
}
