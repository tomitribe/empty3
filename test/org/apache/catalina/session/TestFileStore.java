/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.catalina.session;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.tomcat.util.http.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.catalina.Manager;
import org.apache.tomcat.unittest.TesterContext;
import org.apache.tomcat.unittest.TesterServletContext;


public class TestFileStore {

    private static final String SESS_TEMPPATH = "SESS_TEMP";
    private static final String NON_SESS_TEMPPATH = "NON_SESS_TEMPPATH";
    private static final File dir = new File(SESS_TEMPPATH);
    private static final File dir_non_sess = new File(NON_SESS_TEMPPATH);
    private static FileStore fileStore;
    private static File file1 = new File(SESS_TEMPPATH + "/tmp1.session");
    private static File file2 = new File(SESS_TEMPPATH + "/tmp2.session");
    private static File file3 = new File(NON_SESS_TEMPPATH + "/tmp3.session");
    private static Manager manager = new StandardManager();


    @BeforeClass
    public static void setup() {
        TesterContext testerContext = new TesterContext();
        testerContext.setServletContext(new TesterServletContext());
        manager.setContainer(testerContext);
        fileStore = new FileStore();
        fileStore.setManager(manager);
    }


    @AfterClass
    public static void cleanup() throws IOException {
        if (dir.exists()) {
            FileUtils.cleanDirectory(dir);
            FileUtils.deleteDirectory(dir);
        }
        if (dir_non_sess.exists()) {
            FileUtils.cleanDirectory(dir_non_sess);
            FileUtils.deleteDirectory(dir_non_sess);
        }
    }

    @After
    public void afterEachTest() throws IOException {
        FileUtils.cleanDirectory(dir);
        FileUtils.deleteDirectory(dir);
        FileUtils.cleanDirectory(dir_non_sess);
        FileUtils.deleteDirectory(dir_non_sess);
    }


    @Before
    public void beforeEachTest() throws IOException {
        fileStore.setDirectory(SESS_TEMPPATH);
        if (!dir.mkdir()) {
            Assert.fail();
        }
        if (!file1.createNewFile()) {
            Assert.fail();
        }
        if (!file2.createNewFile()) {
            Assert.fail();
        }
        if (!dir_non_sess.mkdir()) {
            Assert.fail();
        }
        if (!file3.createNewFile()) {
            Assert.fail();
        }
    }


    @Test
    public void getSize() throws Exception {
        Assert.assertEquals(2, fileStore.getSize());
    }


    @Test
    public void clear() throws Exception {
        fileStore.clear();
        Assert.assertEquals(0, fileStore.getSize());
    }

    @Test
    public void keys() throws Exception {
        String[] keys = fileStore.keys();
        //Need it since FileStore.keys doesn't guarantee any specific array order.
        Arrays.sort(keys);
        Assert.assertArrayEquals(new String[]{"tmp1", "tmp2"}, keys);
        fileStore.clear();
        Assert.assertArrayEquals(new String[]{}, fileStore.keys());
    }


    @Test
    public void removeTest() throws Exception {
        fileStore.remove("tmp1");
        Assert.assertEquals(1, fileStore.getSize());
    }


    @Test
    public void loadInvalidPathTest() throws Exception {
        Assert.assertNull(fileStore.load("../" + NON_SESS_TEMPPATH + "/tmp3"));
    }

    @Test
    public void saveInvalidPathTest() throws Exception {
        fileStore.remove("../" + NON_SESS_TEMPPATH + "/tmp3");
        //Since is an invalid Path, remove shouldn't be processed.
        Assert.assertTrue(file3.exists());
    }

}