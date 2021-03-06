package io.github.oemergenc.hbase.orm.extensions.componenttests

import io.github.oemergenc.hbase.orm.extensions.dao.MultipleDynamicColumsDao
import io.github.oemergenc.hbase.orm.extensions.domain.dto.DependentWithListType
import io.github.oemergenc.hbase.orm.extensions.domain.dto.DependentWithPrimitiveTypes
import io.github.oemergenc.hbase.orm.extensions.domain.records.MultipleHBDynamicColumnsRecord
import spock.lang.Unroll

import static io.github.oemergenc.hbase.orm.extensions.data.TestContent.getDefaultMultiHBDynamicColumRecord
import static io.github.oemergenc.hbase.orm.extensions.data.TestContent.getStubDependend

class MultipleDynamicColumnsDaoComponentTest extends AbstractComponentSpec {
    def dao = new MultipleDynamicColumsDao(bigTableHelper.connect())

    def "Store and reading dynamic column works"() {
        given:
        def expectedRowKey = "theRowKey"
        def dynamicValues1 = [
                new DependentWithPrimitiveTypes("dv1_dp1_1", "dv1_dp2_1", "dv1_position_1", "dv1_recipeId_1", "<html>dv1_html_1</html>"),
                new DependentWithPrimitiveTypes("dv1_dp1_2", "dv1_dp2_2", "dv1_position_2", "dv1_recipeId_2", "<html>dv1_html_2</html>")
        ]
        def dynamicValues2 = [
                new DependentWithListType("dv2_dp1_1", "otherId_1", [], "aString_1"),
                new DependentWithListType("dv2_dp1_2", "otherId_2", [], "aString_2"),
        ]
        def dynamicValues3 = [
                new DependentWithPrimitiveTypes("dv3_dp1_1", "dv3_dp2_1", "dv3_position_1", "dv3_recipeId_1", "<html>dv3_html_1</html>"),
                new DependentWithPrimitiveTypes("dv3_dp1_2", "dv3_dp2_2", "dv3_position_2", "dv3_recipeId_2", "<html>dv3_html_2</html>")
        ]
        def dynamicValues4 = [
                new DependentWithPrimitiveTypes("dv4_dp1_1", "dv4_dp2_1", "dv4_position_1", "dv4_recipeId_1", "<html>dv4_html_1</html>"),
                new DependentWithPrimitiveTypes("dv4_dp1_2", "dv4_dp2_2", "dv4_position_2", "dv4_recipeId_2", "<html>dv4_html_2</html>")
        ]

        def record = new MultipleHBDynamicColumnsRecord(expectedRowKey, dynamicValues1, dynamicValues2, dynamicValues3, dynamicValues4)

        when:
        def rowKey = dao.persist(record)

        then:
        rowKey == expectedRowKey

        when:
        def recordResult = dao.get(expectedRowKey)

        then:
        recordResult
        recordResult.staticId == rowKey

        and:
        recordResult.dynamicValues1.collect { it.dynamicPart1 }.containsAll(["dv1_dp1_1", "dv1_dp1_2"])
        recordResult.dynamicValues1.collect { it.dynamicPart2 }.containsAll(["dv1_dp2_1", "dv1_dp2_2"])
        recordResult.dynamicValues1.collect { it.position }.containsAll(["dv1_position_1", "dv1_position_2"])
        recordResult.dynamicValues1.collect { it.recipeId }.containsAll(["dv1_recipeId_1", "dv1_recipeId_2"])
        recordResult.dynamicValues1.collect { it.html }.containsAll(["<html>dv1_html_1</html>", "<html>dv1_html_2</html>"])

        and:
        recordResult.dynamicValues2.collect { it.dynamicPart1 }.containsAll(["dv2_dp1_1", "dv2_dp1_2"])
        recordResult.dynamicValues2.collect { it.otherId }.containsAll(["otherId_1", "otherId_2"])
        recordResult.dynamicValues2.collect { it.aString }.containsAll(["aString_1", "aString_2"])
        recordResult.dynamicValues2.flatten { it.nodes } == []

        and:
        recordResult.dynamicValues3.collect { it.dynamicPart1 }.containsAll(["dv3_dp1_1", "dv3_dp1_2"])
        recordResult.dynamicValues3.collect { it.dynamicPart2 }.containsAll(["dv3_dp2_1", "dv3_dp2_2"])
        recordResult.dynamicValues3.collect { it.position }.containsAll(["dv3_position_1", "dv3_position_2"])
        recordResult.dynamicValues3.collect { it.recipeId }.containsAll(["dv3_recipeId_1", "dv3_recipeId_2"])
        recordResult.dynamicValues3.collect { it.html }.containsAll(["<html>dv3_html_1</html>", "<html>dv3_html_2</html>"])

        and:
        recordResult.dynamicValues4.collect { it.dynamicPart1 }.containsAll(["dv4_dp1_1", "dv4_dp1_2"])
        recordResult.dynamicValues4.collect { it.dynamicPart2 }.containsAll(["dv4_dp2_1", "dv4_dp2_2"])
        recordResult.dynamicValues4.collect { it.position }.containsAll(["dv4_position_1", "dv4_position_2"])
        recordResult.dynamicValues4.collect { it.recipeId }.containsAll(["dv4_recipeId_1", "dv4_recipeId_2"])
        recordResult.dynamicValues4.collect { it.html }.containsAll(["<html>dv4_html_1</html>", "<html>dv4_html_2</html>"])
    }

    @Unroll
    def "Empty lists or null values in dynamic list do not break persisting valid values"() {
        given:
        def expectedRowKey = UUID.randomUUID() as String
        def dynamicValues1 = dynamicList
        def dynamicValues2 = [new DependentWithListType("dv2_dp1_1", "otherId_1", [], "aString_1")]
        def dynamicValues3 = [new DependentWithPrimitiveTypes("dv3_dp1_1", "dv3_dp2_1", "dv3_position_1", "dv3_recipeId_1", "<html>dv3_html_1</html>")]
        def dynamicValues4 = [new DependentWithPrimitiveTypes("dv4_dp1_1", "dv4_dp2_1", "dv4_position_1", "dv4_recipeId_1", "<html>dv4_html_1</html>"),
                              new DependentWithPrimitiveTypes("dv4_dp1_2", "dv4_dp2_2", "dv4_position_2", "dv4_recipeId_2", "<html>dv4_html_2</html>")]

        def record = new MultipleHBDynamicColumnsRecord(expectedRowKey, dynamicValues1, dynamicValues2, dynamicValues3, dynamicValues4)

        when:
        def rowKey = dao.persist(record)

        then:
        rowKey == expectedRowKey

        when:
        def recordResult = dao.get(expectedRowKey)

        then:
        recordResult
        recordResult.staticId == rowKey

        and:
        recordResult.dynamicValues1 == expectedDynamicList

        and:
        recordResult.dynamicValues2.collect { it.dynamicPart1 }.containsAll(["dv2_dp1_1"])
        recordResult.dynamicValues2.collect { it.otherId }.containsAll(["otherId_1"])
        recordResult.dynamicValues2.collect { it.aString }.containsAll(["aString_1"])
        recordResult.dynamicValues2.flatten { it.nodes } == []

        and:
        recordResult.dynamicValues3.collect { it.dynamicPart1 }.containsAll(["dv3_dp1_1"])
        recordResult.dynamicValues3.collect { it.dynamicPart2 }.containsAll(["dv3_dp2_1"])
        recordResult.dynamicValues3.collect { it.position }.containsAll(["dv3_position_1"])
        recordResult.dynamicValues3.collect { it.recipeId }.containsAll(["dv3_recipeId_1"])
        recordResult.dynamicValues3.collect { it.html }.containsAll(["<html>dv3_html_1</html>"])

        and:
        recordResult.dynamicValues4.collect { it.dynamicPart1 }.containsAll(["dv4_dp1_1", "dv4_dp1_2"])
        recordResult.dynamicValues4.collect { it.dynamicPart2 }.containsAll(["dv4_dp2_1", "dv4_dp2_2"])
        recordResult.dynamicValues4.collect { it.position }.containsAll(["dv4_position_1", "dv4_position_2"])
        recordResult.dynamicValues4.collect { it.recipeId }.containsAll(["dv4_recipeId_1", "dv4_recipeId_2"])
        recordResult.dynamicValues4.collect { it.html }.containsAll(["<html>dv4_html_1</html>", "<html>dv4_html_2</html>"])

        where:
        dynamicList                                               | expectedDynamicList
        [null]                                                    | null
        []                                                        | null
        [null, getStubDependend()]                                | [getStubDependend()]
        [getStubDependend(dynamicPart1: null)]                    | null
        [getStubDependend(dynamicPart1: "")]                      | null
        [getStubDependend(dynamicPart1: ""), getStubDependend()]  | [getStubDependend()]
        [getStubDependend(dynamicPart1: " "), getStubDependend()] | [getStubDependend()]
    }

    def "Querying for a specific dynamic qualifier works"() {
        given:
        def expectedRowKey = "theRowKey"
        def dynamicValues1 = [
                new DependentWithPrimitiveTypes("dv1_dp1_1", "dv1_dp2_1", "dv1_position_1", "dv1_recipeId_1", "<html>dv1_html_1</html>"),
                new DependentWithPrimitiveTypes("dv1_dp1_2", "dv1_dp2_2", "dv1_position_2", "dv1_recipeId_2", "<html>dv1_html_2</html>")
        ]
        def dynamicValues2 = [
                new DependentWithListType("dv2_dp1_1", "otherId_1", [], "aString_1"),
                new DependentWithListType("dv2_dp1_2", "otherId_2", [], "aString_2"),
        ]
        def dynamicValues3 = [
                new DependentWithPrimitiveTypes("dv3_dp1_1", "dv3_dp2_1", "dv3_position_1", "dv3_recipeId_1", "<html>dv3_html_1</html>"),
                new DependentWithPrimitiveTypes("dv3_dp1_2", "dv3_dp2_2", "dv3_position_2", "dv3_recipeId_2", "<html>dv3_html_2</html>")
        ]
        def dynamicValues4 = [
                new DependentWithPrimitiveTypes("dv4_dp1_1", "dv4_dp2_1", "dv4_position_1", "dv4_recipeId_1", "<html>dv4_html_1</html>"),
                new DependentWithPrimitiveTypes("dv4_dp1_2", "dv4_dp2_2", "dv4_position_2", "dv4_recipeId_2", "<html>dv4_html_2</html>")
        ]

        def record = new MultipleHBDynamicColumnsRecord(expectedRowKey, dynamicValues1, dynamicValues2, dynamicValues3, dynamicValues4)

        when:
        def rowKey = dao.persist(record)

        then:
        rowKey == expectedRowKey

        when:
        def recordResult = dao.getDynamicCell(rowKey, "dynamicFamily1", ["dv1_dp1_1", "dv1_dp2_1"])

        then:
        recordResult
        recordResult.staticId == rowKey

        and:
        recordResult.dynamicValues1.size() == 1
        recordResult.dynamicValues1.collect { it.dynamicPart1 }.containsAll(["dv1_dp1_1"])
        recordResult.dynamicValues1.collect { it.dynamicPart2 }.containsAll(["dv1_dp2_1"])
        recordResult.dynamicValues1.collect { it.position }.containsAll(["dv1_position_1"])
        recordResult.dynamicValues1.collect { it.recipeId }.containsAll(["dv1_recipeId_1"])
        recordResult.dynamicValues1.collect { it.html }.containsAll(["<html>dv1_html_1</html>"])
    }

    @Unroll
    def "Converting to get for dynamic qualifiers works"() {
        given:
        def staticId = UUID.randomUUID() as String
        def record = getDefaultMultiHBDynamicColumRecord(staticId)

        when:
        dao.persist(record)

        then:
        def resultResult = dao.getRecordForDynamicFamily(staticId, queriedQualifierParts)

        then:
        resultResult.isPresent() == isPresent

        where:
        queriedQualifierParts      | isPresent
        ["dv1_dp1_1", "dv1_dp2_1"] | true
        ["dynamicId1"]             | false
        ["dv1_dp1_1", null]        | false
        ["", "dv1_dp2_1"]          | false
        ["   ", "dv1_dp2_1"]       | false
        ["237912837"]              | false
        [" "]                      | false
    }
}
