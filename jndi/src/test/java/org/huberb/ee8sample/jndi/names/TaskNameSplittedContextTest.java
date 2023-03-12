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
package org.huberb.ee8sample.jndi.names;

import java.util.List;
import java.util.function.Consumer;
import org.huberb.ee8sample.jndi.names.Names.NameSplitted;
import org.huberb.ee8sample.jndi.names.Names.NameSplittedContext;
import org.huberb.ee8sample.jndi.names.Names.Value;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class TaskNameSplittedContextTest {

    static class TaskContextFactory {

        final static String taskRoot = "task";
        final static String hierUser = "task.users.user='%s'";
        final static String hierUserProjects = "task.users.user='%s'.projects";
        final static String hierUserProject = "task.users.user='%s'.projects.project='%s'";

        private TaskContextFactory() {
        }

        static Consumer<NameSplittedContext> root() {
            return (tc) -> {
                tc.put(new NameSplitted(taskRoot), Value.empty());
            };

        }

        static Consumer<NameSplittedContext> user(String user) {
            return (tc) -> {
                tc.put(new NameSplitted(String.format(hierUser, user)), Value.empty());
            };
        }

        static Consumer<NameSplittedContext> project(String user, String project) {
            return (tc) -> {
                tc.put(new NameSplitted(String.format(hierUserProject, user, project)), Value.empty());
            };
        }
    }

    @Test
    public void hello() {

        //---
        NameSplittedContext taskContext = new NameSplittedContext();
        //---
        String user1 = "u1";
        TaskContextFactory.root()
                .andThen(TaskContextFactory.user(user1))
                .andThen(TaskContextFactory.project(user1, "projectA"))
                .andThen(TaskContextFactory.project(user1, "projectB"))
                .andThen(TaskContextFactory.project(user1, "projectX"))
                .accept(taskContext);

        //---
        StringBuilder sb = new StringBuilder();
        taskContext.consume(m -> {
            sb.append(m.toString());
        });
        //System.out.format("taskContext m: %s", sb.toString());

        String expected = "{"
                + "[task, users, u1, projects, projectB]=, "
                + "[task, users, u1, projects, projectA]=, "
                + "[task, users, u1]=, "
                + "[task, users, u1, projects, projectX]=, "
                + "[task]="
                + "}";

        assertEquals(expected, sb.toString());

        List foundProjects = taskContext.findPrefix(new NameSplitted(String.format(TaskContextFactory.hierUserProjects, user1)));
        assertEquals(3, foundProjects.size());
        String expectedFoundProjects2 = "["
                + "[task, users, u1, projects, projectB]=, "
                + "[task, users, u1, projects, projectA]=, "
                + "[task, users, u1, projects, projectX]="
                + "]";
        assertEquals(expectedFoundProjects2, foundProjects.toString());
    }
}
