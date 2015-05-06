package com.flipkart.databuilderframework.model;

import java.util.HashSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.junit.Assert;
import org.junit.Test;

public class DataBuilderMetaTest {
    @Test
    public void testEquals1() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        Assert.assertEquals(lhs, rhs);
        Assert.assertEquals(lhs.hashCode(), rhs.hashCode());
    }

    @Test
    public void testEquals2() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), new HashSet<String>(), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), new HashSet<String>(), "C", "test");
        Assert.assertEquals(lhs, rhs);
        Assert.assertEquals(lhs.hashCode(), rhs.hashCode());
    }

    
    @Test
    public void testNotEquals1() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test1");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals2() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals3() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "X"), ImmutableSet.of("O"), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals9() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("D"), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }
    
    @Test
    public void testNotEquals10() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O","A"), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }
    
    @Test
    public void testNotEquals11() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of(""), "C", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }
    
    @Test
    public void testNotEquals4() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"), ImmutableSet.of("O"),"C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A"), ImmutableSet.of("O"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
    }

    @Test
    public void testNotEquals5() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("O"), "C", "test");
        DataBuilderMeta rhs = new DataBuilderMeta(ImmutableSet.of("A", "X"),ImmutableSet.of("O"), "D", "test");
        Assert.assertFalse(lhs.equals(rhs));
        Assert.assertFalse(lhs.hashCode() == rhs.hashCode());
    }

    @Test
    public void testNotEquals6() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("O"), "C", "test");
        Assert.assertTrue(lhs.equals(lhs));
    }

    @Test
    public void testNotEquals7() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("O"), "C", "test");
        Assert.assertFalse(lhs.equals(null));
    }

    @Test
    public void testNotEquals8() {
        DataBuilderMeta lhs = new DataBuilderMeta(ImmutableSet.of("A", "B"),ImmutableSet.of("O"), "C", "test");
        Assert.assertFalse(lhs.equals(new Integer(100)));
    }

}
