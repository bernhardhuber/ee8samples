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
package org.huberb.pureko.application.support;

import java.util.Random;
import java.util.function.Supplier;

/**
 *
 * @author berni3
 */
public class RandomChoosing {

    public static Supplier<Integer> randomlyChoose(int numberOfOptions) {
        return () -> {
            final int randRange;
            if (numberOfOptions < 100) {
                randRange = 100;
            } else {
                randRange = numberOfOptions;
            }
            final int choosen = new Random().nextInt(randRange) % numberOfOptions;
            return choosen;
        };
    }

}
