package com.flipkart.databuilderframework.model;

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
    public void testEquals2() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test");
        Assert.assertTrue(lhs.equals(lhs));
    }

    @Test
    public void testEquals3() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1", "OP2"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1", "OP2"), "C", "test");
        Assert.assertEquals(lhs, rhs);
        Assert.assertEquals(lhs.hashCode(), rhs.hashCode());
    }

    @Test
    public void testEquals4() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP2"), "C", "test");
        Assert.assertTrue(lhs.equals(lhs));
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
    public void testNotEquals9() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1", "OP2"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test1");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals10() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1", "OP2"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1"), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals11() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("OP1"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "R"),ImmutableSet.of("OP1"),  "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }
    @Test
    public void testNotEquals12() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("OP1"),  "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals13() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("OP1"),  "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("OP1"), "C", "test1");
        Assert.assertFalse(lhs.equals(rhs));
    }
}
