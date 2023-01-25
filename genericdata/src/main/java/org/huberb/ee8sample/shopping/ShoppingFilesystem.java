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
import java.util.function.Function;
import java.util.stream.Collectors;
import org.huberb.ee8sample.fs.Filesystem;
import org.huberb.ee8sample.fs.Filesystem.Files.AbstractFile;
import org.huberb.ee8sample.fs.Filesystem.Files.AbstractFile.FileType;
import org.huberb.ee8sample.fs.Filesystem.Files.Directory;
import org.huberb.ee8sample.fs.Filesystem.Files.RegularPayload;
import org.huberb.ee8sample.genericdata.Basics.LoginUser;
import org.huberb.ee8sample.genericdata.Basics.Person;
import org.huberb.ee8sample.shopping.Shoppings.ShoppingItem;
import org.huberb.ee8sample.shopping.Shoppings.StockItem;

/**
 *
 * @author berni3
 */
public class ShoppingFilesystem {

    Directory rootDirectory = new Directory(Filesystem.Files.Directory.ROOT_DIRECTORY_NAME);
    Directory stockItemsDirectoriesRootDirectory = createDirectory(rootDirectory, "StockItems");
    Directory shoppingCardsRootDirectory = createDirectory(rootDirectory, "ShoppingCards");
    Directory loggedInUserRootDirectory = createDirectory(rootDirectory, "LoggedInUsers");

    static Directory createDirectory(Directory baseDir, String dirName) {
        final Directory d = new Directory(dirName);
        baseDir.add(d);
        return d;
    }

    static <T> RegularPayload<T> createFile(Directory baseDir, String tname, T t) {
        final RegularPayload<T> rp = new RegularPayload<>(tname, baseDir, t);
        baseDir.add(rp);
        return rp;
    }

    static class StockItems {

        Directory createStockItemsDirectory(Directory baseDir, String dirName) {
            final Directory d = createDirectory(baseDir, dirName);
            return d;
        }

        Directory addStockItems(Directory stockItemsParent, List<StockItem> l) {
            for (StockItem stockItem : l) {
                RegularPayload<StockItem> rp = createFile(stockItemsParent, stockItem.getItem().getItemCode(), stockItem);
                stockItemsParent.add(rp);
            }
            return stockItemsParent;
        }
    }

    static class ShoppingCards {

        static Function<Directory, List<AbstractFile>> f(String prefix) {
            return (baseDir) -> {
                List<AbstractFile> afList = baseDir.getFiles().stream()
                        .filter((f) -> f.getFileType() == FileType.regular)
                        .filter((f) -> f.getName().startsWith(prefix))
                        .collect(Collectors.toList());
                return afList;
            };
        }

        static Function<Directory, List<AbstractFile>> d(String prefix) {
            return (baseDir) -> {
                List<AbstractFile> afList = baseDir.getFiles().stream()
                        .filter((f) -> f.getFileType() == FileType.directory)
                        .filter((f) -> f.getName().startsWith(prefix))
                        .collect(Collectors.toList());
                return afList;
            };
        }

        Directory createShoppingCardDirectory(Directory baseDir, String shoppingCardName) {
            Directory shoppingCardDirectory = createDirectory(baseDir, shoppingCardName);
            return shoppingCardDirectory;
        }

        RegularPayload<Person> addPerson(Directory baseDir, Person person) {
            List<AbstractFile> afPersonList = f("person_").apply(baseDir);
            for (AbstractFile af : afPersonList) {
                baseDir.getFiles().remove(af);
            }

            String personFilename = "person_" + person.getPersonName().getFirstName() + "_" + person.getPersonName().getLastName();
            RegularPayload<Person> f = createFile(baseDir, personFilename, person);
            return f;
        }

        RegularPayload<LoginUser> addLoginUser(Directory baseDir, LoginUser lu) {
            List<AbstractFile> afPersonList = f("loging_user_").apply(baseDir);
            for (AbstractFile af : afPersonList) {
                baseDir.getFiles().remove(af);
            }
            String loginUserFilename = "loging_user_" + lu.getUserName();
            RegularPayload<LoginUser> f = createFile(baseDir, loginUserFilename, lu);
            return f;
        }

        void addShoppingItems(Directory baseDir, List<ShoppingItem> shoppingItems) {
            Directory shoppingItemsDir;
            List<AbstractFile> afList = d("ShoppingItems").apply(baseDir);
            if (afList.isEmpty()) {
                shoppingItemsDir = createDirectory(baseDir, "ShoppingItems");
            } else {
                shoppingItemsDir = (Directory) afList.get(0);
            }
            for (ShoppingItem shoppingItem : shoppingItems) {
                String shoppingItemFilename = "shoppingItem_" + shoppingItem.getItem().getItemCode();
                createFile(baseDir, shoppingItemFilename, shoppingItem);
            }
        }
    }

    static class LoggedInUsers {

        void addLoggedInUser() {

        }

        void removeLoggedInUser() {
        }
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
