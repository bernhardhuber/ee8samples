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
package org.huberb.ee8sample.shopping;

import java.util.List;
import java.util.function.BiFunction;
import org.huberb.ee8sample.fs.Filesystem;
import org.huberb.ee8sample.fs.Filesystem.Files.AbstractFile;
import org.huberb.ee8sample.fs.Filesystem.Files.AbstractFile.FileType;
import org.huberb.ee8sample.fs.Filesystem.Files.Directory;
import org.huberb.ee8sample.fs.Filesystem.Files.RegularPayload;
import org.huberb.ee8sample.genericdata.Basics.Person;
import org.huberb.ee8sample.shopping.Shoppings.StockItem;

/**
 *
 * @author berni3
 */
public class ShoppingFilesystem {

    Directory rootDirectory = Filesystem.Files.Directory.ROOT_DIRECTORY;
    Directory stockItemsDirectoriesRootDirectory = new Directory("StockItems", rootDirectory);
    Directory personsRootDirectory = new Directory("Persons");
    Directory loggedInUserRootDirectory = new Directory("LoggedInUsers");

    Directory createStockItemsDirectory(String n) {
        final Directory d = new Directory(n, stockItemsDirectoriesRootDirectory);
        stockItemsDirectoriesRootDirectory.add(d);
        return d;
    }

    Directory addStockItems(Directory stockItemsParent, List<StockItem> l) {
        for (StockItem stockItem : l) {
            RegularPayload<StockItem> rp = new RegularPayload<>(stockItem.getItem().getItemCode(), stockItemsParent, stockItem);
            stockItemsParent.add(rp);
        }
        return stockItemsParent;
    }

    RegularPayload<Person> addPerson(Person person) {
        RegularPayload<Person> f = new RegularPayload<>(person.getPersonName().toString(), personsRootDirectory, person);
        personsRootDirectory.add(f);
        return f;
    }

    void addLoggedInUser() {

    }

    void removeLoggedInUser() {
    }

    void dumpRootDirectory() {
        dumpDirectory(0, rootDirectory);
    }

    void dumpDirectory(int i, Directory d) {
        BiFunction<String, Integer, String> f = (prefix, indent) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            for (int j = 0; j < indent; j += 1) {
                sb.append(' ');
            }
            return sb.toString();
        };

        System.out.format("%sdirectory: { level: %d, name: %s}%n", f.apply("> ", i), i, d.getName());
        d.getFiles().forEach((AbstractFile af) -> {
            if (af.getFileType() == FileType.directory) {
                dumpDirectory(i + 1, (Directory) af);
            } else if (af.getFileType() == FileType.regular) {
                System.out.format("%sregular: { level: %d, name: %s}%n", f.apply(" *", i), i, af.getName());
            }
        });
    }
}
