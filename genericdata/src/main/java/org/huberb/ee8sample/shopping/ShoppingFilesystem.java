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
import org.huberb.ee8sample.fs.Filesystem.Files.Commands;
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

    final Directory rootDirectory = new Directory(Filesystem.Files.Directory.ROOT_DIRECTORY_NAME);
    final Directory stockItemsDirectoriesRootDirectory = Commands.createDirectory(rootDirectory, "StockItems");
    final Directory shoppingCardsRootDirectory = Commands.createDirectory(rootDirectory, "ShoppingCards");
    final Directory loggedInUserRootDirectory = Commands.createDirectory(rootDirectory, "LoggedInUsers");
    final Directory ordersRootDirectory = Commands.createDirectory(rootDirectory, "Orders");
    final Directory deliveriesRootDirectory = Commands.createDirectory(rootDirectory, "Deliveries");

    static class StockItems {

        Directory createStockItemsDirectory(Directory baseDir, String dirName) {
            final Directory d = Commands.createDirectory(baseDir, dirName);
            return d;
        }

        Directory addStockItems(Directory stockItemsParent, List<StockItem> l) {
            for (StockItem stockItem : l) {
                RegularPayload<StockItem> rp = Commands.createFile(stockItemsParent, stockItem.getItem().getItemCode(), stockItem);
                stockItemsParent.add(rp);
            }
            return stockItemsParent;
        }
    }

    static class ShoppingCards {

        Directory createShoppingCardDirectory(Directory baseDir, String shoppingCardName) {
            Directory shoppingCardDirectory = Commands.createDirectory(baseDir, shoppingCardName);
            return shoppingCardDirectory;
        }

        RegularPayload<Person> addPerson(Directory baseDir, Person person) {
            List<AbstractFile> afPersonList = Commands.findRegularFilesByPrefix(baseDir, "person_");
            for (AbstractFile af : afPersonList) {
                baseDir.getFiles().remove(af);
            }

            String personFilename = "person_" + person.getPersonName().getFirstName() + "_" + person.getPersonName().getLastName();
            RegularPayload<Person> f = Commands.createFile(baseDir, personFilename, person);
            return f;
        }

        RegularPayload<LoginUser> addLoginUser(Directory baseDir, LoginUser lu) {
            List<AbstractFile> afPersonList = Commands.findRegularFilesByPrefix(baseDir, "loging_user_");
            for (AbstractFile af : afPersonList) {
                baseDir.getFiles().remove(af);
            }
            String loginUserFilename = "loging_user_" + lu.getUserName();
            RegularPayload<LoginUser> f = Commands.createFile(baseDir, loginUserFilename, lu);
            return f;
        }

        void addShoppingItems(Directory baseDir, List<ShoppingItem> shoppingItems) {
            Directory shoppingItemsDir;
            List<AbstractFile> afList = Commands.findDirsByPrefix(baseDir, "ShoppingItems");
            if (afList.isEmpty()) {
                shoppingItemsDir = Commands.createDirectory(baseDir, "ShoppingItems");
            } else {
                shoppingItemsDir = (Directory) afList.get(0);
            }
            for (ShoppingItem shoppingItem : shoppingItems) {
                String shoppingItemFilename = "shoppingItem_" + shoppingItem.getItem().getItemCode();
                Commands.createFile(baseDir, shoppingItemFilename, shoppingItem);
            }
        }
    }

    static class LoggedInUsers {

        void addLoggedInUser() {

        }

        void removeLoggedInUser() {
        }
    }

    static class SupportCommands {

        public static void dumpDirectory(Directory baseDir) {
            dumpDirectory(0, baseDir);
        }

        static void dumpDirectory(int i, Directory d) {
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
                    System.out.format("%sregular: { level: %d, name: %s}%n", f.apply("*  ", i), i, af.getName());
                }
            });
        }
    }
}
