package io.appform.databuilderframework.model;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

public class DataBuilderMetaTest {
    @Test
    public void testEquals() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        Assert.assertEquals(lhs, rhs);
        Assert.assertEquals(lhs.hashCode(), rhs.hashCode());
    }
    
    @Test
    public void testEqualsWithOptionalsAndAccess() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O"),ImmutableSet.of("G"));
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O"),ImmutableSet.of("G"));
        Assert.assertEquals(lhs, rhs);
        Assert.assertEquals(lhs.hashCode(), rhs.hashCode());
    }


    
    @Test
    public void testNotEquals1() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test1");
        Assert.assertFalse(lhs.equals(rhs));
    }
    
    @Test
    public void testNotEquals2() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals3() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "X"), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals4() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals5() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "X"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
        Assert.assertFalse(lhs.hashCode() == rhs.hashCode());
    }

    @Test
    public void testNotEquals6() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        Assert.assertTrue(lhs.equals(lhs));
    }

    @Test
    public void testNotEquals7() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        Assert.assertFalse(lhs.equals(null));
    }

    @Test
    public void testNotEquals8() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        Assert.assertFalse(lhs.equals(new Integer(100)));
    }
    
    @Test
    public void testNotEqualsWithOptionalsAndAccess1() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O","H"),ImmutableSet.of("G"));
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O"),ImmutableSet.of("G"));
        Assert.assertFalse(lhs.equals(rhs));
    }
    
    @Test
    public void testNotEqualsWithOptionalsAndAccess2() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O"),ImmutableSet.of("G"));
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O"),ImmutableSet.of("H"));
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEqualsWithOptionalsAndAccess3() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test",ImmutableSet.of("O"),ImmutableSet.of("G"));
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }
}
