package io.github.oemergenc.hbase.orm.extensions

import com.flipkart.hbaseobjectmapper.DynamicQualifier
import spock.lang.Specification

class WrappedHBDynamicColumnSpec extends Specification {

    def "Valid family and qualifier works"() {
        given:
        def clazz = new ValidHBDynamicColumnClass()
        def field = clazz.getClass().getDeclaredField("field")

        when:
        def column = new WrappedHBDynamicColumn(field)

        then:
        noExceptionThrown()
        column.isPresent == true
        column.family == "f"
        column.columnQualifierField == "q"
        column.alias == "q"
        column.seperator == "#"
        column.prefix == "q#"
    }
//
//    def "no HBDynamicColumn annotation does not throw exception"() {
//        given:
//        def clazz = new NoHBDynamicColumnClass()
//        def field = clazz.getClass().getDeclaredField("field")
//
//        when:
//        def column = new WrappedHBDynamicColumn(field)
//
//        then:
//        noExceptionThrown()
//        column.isPresent == false
//        column.family == null
//        column.columnQualifierField == null
//        column.alias == null
//        column.seperator == null
//    }
//
//    @Unroll
//    def "wrong field type throws exception"() {
//        given:
//        def field = clazz.getClass().getDeclaredField("field")
//
//        when:
//        new WrappedHBDynamicColumn(field)
//
//        then:
//        thrown(IllegalArgumentException)
//
//        where:
//        clazz                                          | _
//        new WrongPrimitiveHBDynamicColumnClass()       | _
//        new WrongComplexHBDynamicColumnClass()         | _
//        new WrongListComplexTypeHBDynamicColumnClass() | _
//    }
//
//    @Unroll
//    def "invalid qualifier field throws exception"() {
//        given:
//        def field = clazz.getClass().getDeclaredField("field")
//
//        when:
//        new WrappedHBDynamicColumn(field)
//
//        then:
//        thrown(IllegalArgumentException)
//
//        where:
//        clazz                                    | _
//        new EmptyQualifierHBDynamicColumnClass() | _
//        new BlankQualifierHBDynamicColumnClass() | _
//    }
//
//    @Unroll
//    def "wrong list field type throws exception"() {
//        given:
//        def field = clazz.getClass().getDeclaredField("field")
//
//        when:
//        new WrappedHBDynamicColumn(field)
//
//        then:
//        thrown(IllegalArgumentException)
//
//        where:
//        clazz                                          | _
//        new WrongListPrimitiveHBDynamicColumnClass()   | _
//        new WrongListComplexTypeHBDynamicColumnClass() | _
//    }

    class SimpleQualifierClass {
        String q;
    }

    class ValidHBDynamicColumnClass {
        @HBDynamicColumn(family = "f",
                alias = "alias",
                qualifier = @DynamicQualifier(parts = ["q"], composer = "composer", parser = "parser"))
        List<SimpleQualifierClass> field;
    }
//
//    class EmptyQualifierHBDynamicColumnClass {
//        @HBDynamicColumn(family = "f", qualifierField = " ")
//        List<SimpleQualifierClass> field;
//    }
//
//    class BlankQualifierHBDynamicColumnClass {
//        @HBDynamicColumn(family = "f", qualifierField = "")
//        List<SimpleQualifierClass> field;
//    }
//
//    class ListHBDynamicColumnClass {
//        @HBDynamicColumn(family = "", qualifierField = "q")
//        List<String> field;
//    }
//
//    class WrongPrimitiveHBDynamicColumnClass {
//        @HBDynamicColumn(family = "", qualifierField = "q")
//        String field;
//    }
//
//    class WrongComplexHBDynamicColumnClass {
//        @HBDynamicColumn(family = "", qualifierField = "q")
//        Map<String, String> field;
//    }
//
//    class WrongListPrimitiveHBDynamicColumnClass {
//        @HBDynamicColumn(family = "", qualifier = "q")
//        List<Integer> field;
//    }
//
//    class WrongListComplexTypeHBDynamicColumnClass {
//        @HBDynamicColumn(family = "", qualifierField = "q")
//        List<Map<String, String>> field;
//    }

    class NoHBDynamicColumnClass {
        def field;
    }
}

