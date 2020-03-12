package io.github.oemergenc.hbase.orm.extensions.mapper

import io.github.oemergenc.hbase.orm.extensions.HBDynamicColumnObjectMapper
import io.github.oemergenc.hbase.orm.extensions.domain.dto.DependentWithMap
import io.github.oemergenc.hbase.orm.extensions.domain.records.SingleHBDynamicColumnRecord
import spock.lang.Specification

class SingleDynamicColumnMapperSpec extends Specification {
    def mapper = new HBDynamicColumnObjectMapper()

    def "Converting a record with a single dynamic column works"() {
        given:
        def staticId = "staticId"
        def dynamicFamilyList = [
                new DependentWithMap("dynamicId2", ["1sja": "bsjcasla", "12321": "csdckqewwqe"]),
                new DependentWithMap("dynamicId1", ["1": "bla", "12321": "qewwqe"])
        ]
        def record = new SingleHBDynamicColumnRecord(staticId, dynamicFamilyList)

        when:
        def result = mapper.writeValueAsResult(record)

        then:
        result
        result.getFamilyMap("dynamicFamily".bytes)["id#dynamicId1".bytes]
        result.getFamilyMap("dynamicFamily".bytes)["id#dynamicId2".bytes]
        result.getFamilyMap("staticFamily".bytes)['staticId'.bytes]

        when:
        def recordResult = mapper.readValue(result, SingleHBDynamicColumnRecord.class)

        then:
        recordResult
        recordResult.staticId == staticId
        recordResult.dynamicFamily.size() == 2
        recordResult.dynamicFamily.collect { it.dynamicId }.containsAll(["dynamicId1", "dynamicId2"])
    }
}
