package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.DataBuilderContext;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestContextGetSet {
    private static final class TestClass {
        private String name;
        private int age;

        private TestClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestClass testClass = (TestClass) o;

            if (age != testClass.age) return false;
            if (!name.equals(testClass.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }
    }

    @Test
    public void testContextGetSet() {
        DataBuilderContext context = new DataBuilderContext();
        TestClass testClass1 = new TestClass("Santanu", 30);
        context.saveContextData("TEST", testClass1);
        TestClass testClass2 = context.getContextData("TEST", TestClass.class);
        Assert.assertEquals(testClass1, testClass2);
    }

    @Test
    public void testContextNotFound() {
        TestClass testClass = new DataBuilderContext().getContextData("TEST", TestClass.class);
        Assert.assertNull(testClass);
    }

    @Test
    public void testNullKey() {
        try {
            new DataBuilderContext().saveContextData(null, new TestClass("XX", 1));
        } catch (RuntimeException e) {
            Assert.assertEquals("Invalid key for context data. Key cannot be null/empty", e.getMessage());
            return;
        }
        fail("Expected exception was not raised");
    }
}
