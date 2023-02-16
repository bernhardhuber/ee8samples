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
import org.huberb.ee8sample.jndi.names.Names.NameSplitted;
import org.huberb.ee8sample.jndi.names.Names.NameSplittedContext;
import org.huberb.ee8sample.jndi.names.Names.Value;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class NameSplittedContextTest {

    @Test
    public void hello() {
        String taskRoot = "task";
        String hierUser = "task.users.user='%s'";
        String hierUserProjects = "task.users.user='%s'.projects";
        String hierUserProject = "task.users.user='%s'.projects.project='%s'";

        //---
        NameSplittedContext taskContext = new NameSplittedContext();
        //---
        taskContext.put(new NameSplitted(taskRoot), Value.empty());
        //---
        String user1 = "u1";
        taskContext.put(new NameSplitted(String.format(hierUser, user1)), Value.empty())
                .put(new NameSplitted(String.format(hierUserProject, user1, "projectA")), Value.empty())
                .put(new NameSplitted(String.format(hierUserProject, user1, "projectB")), Value.empty())
                .put(new NameSplitted(String.format(hierUserProject, user1, "projectX")), Value.empty());

        //---
//        String user2 = "u2";
//        taskContext.put(new NameSplitted(String.format(hierUser, user2)), Value.empty())
//                .put(new NameSplitted(String.format(hierUserProject, user2, "projectA")), Value.empty())
//                .put(new NameSplitted(String.format(hierUserProject, user2, "projectB")), Value.empty())
//                .put(new NameSplitted(String.format(hierUserProject, user2, "projectY")), Value.empty());
        StringBuilder sb = new StringBuilder();
        taskContext.consume(m -> {
            sb.append(m.toString());
        });
        //System.out.format("taskContext m: %s", sb.toString());

        String expected = "{"
                + "[task, users, u1, projects, projectB]=value='', "
                + "[task, users, u1, projects, projectA]=value='', "
                + "[task, users, u1]=value='', "
                + "[task, users, u1, projects, projectX]=value='', "
                + "[task]=value=''"
                + "}";

        assertEquals(expected, sb.toString());

        List foundProjects = taskContext.findPrefix(new NameSplitted(String.format(hierUserProjects, user1)));
        assertEquals(3, foundProjects.size());
        String expectedFoundProjects2 = "["
                + "[task, users, u1, projects, projectB]=value='', "
                + "[task, users, u1, projects, projectA]=value='', "
                + "[task, users, u1, projects, projectX]=value=''"
                + "]";
        assertEquals(expectedFoundProjects2, foundProjects.toString());
    }
}
